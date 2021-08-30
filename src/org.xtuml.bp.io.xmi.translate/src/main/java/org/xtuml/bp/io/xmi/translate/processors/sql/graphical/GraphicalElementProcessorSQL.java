package org.xtuml.bp.io.xmi.translate.processors.sql.graphical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sdmetrics.model.ModelElement;

import org.xtuml.bp.io.xmi.translate.processors.IdProcessor;
import org.xtuml.bp.io.xmi.translate.processors.IgnoreType;
import org.xtuml.bp.io.xmi.translate.processors.generated.AbstractGraphicalElementProcessor;
import org.xtuml.bp.io.xmi.translate.processors.sql.SQLUtils;
import org.xtuml.bp.io.xmi.translate.processors.sql.classes.supporting.ImportedClassProcessorSQL;
import org.xtuml.bp.io.xmi.translate.processors.sql.graphical.supporting.ConnectionInformation;

public class GraphicalElementProcessorSQL extends AbstractGraphicalElementProcessor {

    String elementId = "";
    String importedClassSQL = "";
    UUID importedClassId;
    private String represents;
    private Integer ooaType;
    private boolean skipSupporting;
    private String diagramId;
    public static Map<String, GraphicalElement> createdGraphicalElements = new HashMap<>();

    public GraphicalElementProcessorSQL() {
    }

    public GraphicalElementProcessorSQL(String geId, String represents, String diagramId, int type) {
        this.elementId = geId;
        this.represents = represents;
        this.ooaType = type;
        this.diagramId = diagramId;
        skipSupporting = true;
    }

    @Override
    public String getelementId() {
        // only do this for those coming through the second constructor
        if (!elementId.equals("") && skipSupporting) {
            return SQLUtils.idValue(elementId);
        }
        return SQLUtils.idValue(getModelElement().getPlainAttribute("id"));
    }

    @Override
    public String getdiagramId() {
        if (diagramId != null) {
            return SQLUtils.idValue(diagramId);
        }
        return SQLUtils.idValue(getModelElement().getOwner().getPlainAttribute("id"));
    }

    @Override
    public String getOOA_ID() {
        if (represents != null) {
            return SQLUtils.idValue(represents);
        }
        if (importedClassId != null) {
            return SQLUtils.idValue(importedClassId.toString());
        }
        ConnectionInformation information = ConnectionInformation.connectionMap
                .get(getModelElement().getPlainAttribute("element"));
        if (information != null) {
            if (information.getOoaId() != null) {
                return SQLUtils.idValue(information.getOoaId());
            }
        }
        return SQLUtils.idValue(getModelElement().getPlainAttribute("element"));
    }

    @Override
    public String getOOA_Type() {
        if (ooaType != null) {
            return SQLUtils.numberValue(ooaType);
        }
        if (importedClassId != null) {
            return SQLUtils.numberValue(23);
        }
        int ooaType = getOoaTypeFromOoaId();
        return SQLUtils.numberValue(ooaType);
    }

    @Override
    public String getrepresents() {
        return SQLUtils.numberValue(0);
    }

    @Override
    public String getrepresents_path() {
        return SQLUtils.stringValue("");
    }

    @Override
    public void preprocess(ModelElement element, String keyletters) {
        /* handle imported class cases */
        // if the graphic package and element package do not match, create an imported
        // class
        String actualElePkgId = element.getRefAttribute("actualElement").getOwner().getPlainAttribute("id");
        String graphPkgId = element.getOwner().getRefAttribute("owner").getPlainAttribute("id");
        if (!actualElePkgId.equals(graphPkgId) && getOoaTypeFromOoaId() == 21) {
            // create the imported class entry
            ImportedClassProcessorSQL importedClassProcessorSQL = new ImportedClassProcessorSQL(
                    getModelElement().getOwner().getRefAttribute("owner").getPlainAttribute("id"));
            importedClassProcessorSQL.setModelElement(getModelElement().getRefAttribute("actualElement"));
            importedClassProcessorSQL.setKeyLetters("O_IOBJ");
            importedClassSQL = importedClassProcessorSQL.process(importedClassProcessorSQL.getModelElement(),
                    importedClassProcessorSQL.getKeyLetters());
            importedClassId = importedClassProcessorSQL.getImportedClassId();
        }
        ConnectionInformation information = ConnectionInformation.connectionMap
                .get(getModelElement().getPlainAttribute("element"));
        if (information != null) {
            if (information.getOoaId() != null) {
                elementId = information.getOoaId() + UUID.randomUUID().toString();
            }
        }
        if (elementId == null) {
            elementId = element.getPlainAttribute("id");
        }
    }

    @Override
    public String getProcessorOutput() {
        return SQLUtils.getProcessorOutput(this);
    }

    @Override
    public String createSupportingElements() {
        if (skipSupporting) {
            return "";
        }
        String boundsString = getModelElement().getPlainAttribute("bounds");
        if (isShape()) {
            Rectangle rect = getBoundsFromString(boundsString);
            GraphelementProcessorSQL graphelementProcessorSQL = new GraphelementProcessorSQL(rect.x, rect.y, null);
            graphelementProcessorSQL.setModelElement(getModelElement());
            graphelementProcessorSQL.setKeyLetters("DIM_GE");
            GraphnodeProcessorSQL graphnodeProcessorSQL = new GraphnodeProcessorSQL(rect.w, rect.h, null);
            graphnodeProcessorSQL.setModelElement(getModelElement());
            graphnodeProcessorSQL.setKeyLetters("DIM_ND");
            DiagramelementProcessorSQL diagramelementProcessorSQL = new DiagramelementProcessorSQL();
            diagramelementProcessorSQL.setModelElement(getModelElement());
            diagramelementProcessorSQL.setKeyLetters("DIM_ELE");
            ShapeProcessorSQL shapeProcessorSQL = new ShapeProcessorSQL();
            shapeProcessorSQL.setModelElement(getModelElement());
            shapeProcessorSQL.setKeyLetters("GD_SHP");
            NoncontainingShapeProcessorSQL noncontainingShapeProcessorSQL = new NoncontainingShapeProcessorSQL();
            noncontainingShapeProcessorSQL.setModelElement(getModelElement());
            noncontainingShapeProcessorSQL.setKeyLetters("GD_NCS");
            StringBuilder builder = new StringBuilder(importedClassSQL);
            builder.append(SQLUtils.getInsertStatement(graphelementProcessorSQL, getModelElement()));
            // do not setup dim con unless in same package
            String actualElePkgId = getModelElement().getRefAttribute("actualElement").getOwner()
                    .getPlainAttribute("id");
            String graphPkgId = getModelElement().getOwner().getRefAttribute("owner").getPlainAttribute("id");
            if (actualElePkgId.equals(graphPkgId) || getOoaTypeFromOoaId() != 21) {
                // setup DIM_CON
                List<ConnectionInformation> startConnections = ConnectionInformation.connectionToStartGeMap
                        .get(getModelElement().getPlainAttribute("element"));
                if (startConnections != null) {
                    startConnections.forEach(conn -> {
                        GraphconnectorProcessorSQL graphCon = new GraphconnectorProcessorSQL(
                                getModelElement().getPlainAttribute("id"), rect.getMidPoint().x, rect.getMidPoint().y);
                        graphCon.setKeyLetters("DIM_CON");
                        if (conn.getStartEle().equals(getModelElement().getPlainAttribute("element"))) {
                            conn.setStartCon(graphCon);
                        }
                        builder.append(SQLUtils.getInsertStatement(graphCon, getModelElement()));
                    });
                }
                List<ConnectionInformation> endConnections = ConnectionInformation.connectionToEndGeMap
                        .get(getModelElement().getPlainAttribute("element"));
                if (endConnections != null) {
                    endConnections.forEach(conn -> {
                        GraphconnectorProcessorSQL graphCon = new GraphconnectorProcessorSQL(
                                getModelElement().getPlainAttribute("id"), rect.getMidPoint().x, rect.getMidPoint().y);
                        graphCon.setKeyLetters("DIM_CON");
                        if (conn.getEndEle().equals(getModelElement().getPlainAttribute("element"))) {
                            conn.setEndCon(graphCon);
                        }
                        builder.append(SQLUtils.getInsertStatement(graphCon, getModelElement()));
                    });
                }
            }
            builder.append(SQLUtils.getInsertStatement(graphnodeProcessorSQL, getModelElement()))
                    .append(SQLUtils.getInsertStatement(diagramelementProcessorSQL, getModelElement()))
                    .append(SQLUtils.getInsertStatement(shapeProcessorSQL, getModelElement()))
                    .append(SQLUtils.getInsertStatement(noncontainingShapeProcessorSQL, getModelElement())).toString();
            return builder.toString();
        } else {
            ConnectorDescription descrip = getPolylineFromString(boundsString);
            System.out.println(descrip);
            DiagramelementProcessorSQL diagramelementProcessorSQL = new DiagramelementProcessorSQL();
            diagramelementProcessorSQL.setModelElement(getModelElement());
            diagramelementProcessorSQL.setKeyLetters("DIM_ELE");
            GraphelementProcessorSQL graphelementProcessorSQL = new GraphelementProcessorSQL(0f, 0f, null);
            graphelementProcessorSQL.setModelElement(getModelElement());
            graphelementProcessorSQL.setKeyLetters("DIM_GE");
            ConnectorProcessorSQL connectorProcessorSQL = new ConnectorProcessorSQL();
            connectorProcessorSQL.setModelElement(getModelElement());
            connectorProcessorSQL.setKeyLetters("GD_CON");
            StringBuilder builder = new StringBuilder();
            ConnectionInformation information = ConnectionInformation.connectionMap
                    .get(getModelElement().getPlainAttribute("element"));
            String startId = IdProcessor.NULL_ID;
            String endId = IdProcessor.NULL_ID;
            if (information != null) {
                GraphconnectorProcessorSQL startCon = information.getStartCon();
                GraphconnectorProcessorSQL endCon = information.getEndCon();
                if (startCon != null && endCon != null) {
                    startId = information.getStartCon().getId();
                    endId = information.getEndCon().getId();
                    // create line segment and waypoints
                    WaypointProcessorSQL startWaypointProcessorSQL = new WaypointProcessorSQL(startCon.x, startCon.y,
                            IdProcessor.NULL_ID);
                    startWaypointProcessorSQL.setKeyLetters("DIM_WAY");
                    startWaypointProcessorSQL.setModelElement(getModelElement());
                    WaypointProcessorSQL endWayPointProcessorSQL = new WaypointProcessorSQL(endCon.x, endCon.y,
                            startWaypointProcessorSQL.getId());
                    endWayPointProcessorSQL.setKeyLetters("DIM_WAY");
                    endWayPointProcessorSQL.setModelElement(getModelElement());
                    LineSegmentProcessorSQL lineSegProcessorSQL = new LineSegmentProcessorSQL(
                            getModelElement().getPlainAttribute("id"), startWaypointProcessorSQL.getId(),
                            endWayPointProcessorSQL.getId());
                    lineSegProcessorSQL.setKeyLetters("GD_LS");
                    lineSegProcessorSQL.setModelElement(getModelElement());
                    // setup floating text
                    FloatingTextProcessorSQL startText = new FloatingTextProcessorSQL(0);
                    startText.setKeyLetters("GD_CTXT");
                    startText.setModelElement(getModelElement());
                    // setup floating text
                    FloatingTextProcessorSQL middleText = new FloatingTextProcessorSQL(1);
                    middleText.setKeyLetters("GD_CTXT");
                    middleText.setModelElement(getModelElement());
                    // setup floating text
                    FloatingTextProcessorSQL endText = new FloatingTextProcessorSQL(2);
                    endText.setKeyLetters("GD_CTXT");
                    endText.setModelElement(getModelElement());
                    builder.append(SQLUtils.getInsertStatement(lineSegProcessorSQL, getModelElement()))
                            .append(SQLUtils.getInsertStatement(startWaypointProcessorSQL, getModelElement()))
                            .append(SQLUtils.getInsertStatement(endWayPointProcessorSQL, getModelElement()))
                            .append(startText.getProcessorOutput()).append(middleText.getProcessorOutput())
                            .append(endText.getProcessorOutput());
                } else {
                    if (information.endsOnWs()) {
                        // create linesegment and start waypoint
                        startId = information.getStartCon().getId();
                        // create line segment and waypoints
                        WaypointProcessorSQL startWaypointProcessorSQL = new WaypointProcessorSQL(startCon.x,
                                startCon.y, IdProcessor.NULL_ID);
                        startWaypointProcessorSQL.setKeyLetters("DIM_WAY");
                        startWaypointProcessorSQL.setModelElement(getModelElement());
                        WaypointProcessorSQL endWayPointProcessorSQL = new WaypointProcessorSQL(startCon.x,
                                startCon.y - 100, startWaypointProcessorSQL.getId());
                        endWayPointProcessorSQL.setKeyLetters("DIM_WAY");
                        endWayPointProcessorSQL.setModelElement(getModelElement());
                        LineSegmentProcessorSQL lineSegProcessorSQL = new LineSegmentProcessorSQL(
                                getModelElement().getPlainAttribute("id"), startWaypointProcessorSQL.getId(),
                                endWayPointProcessorSQL.getId());
                        lineSegProcessorSQL.setKeyLetters("GD_LS");
                        lineSegProcessorSQL.setModelElement(getModelElement());
                        // setup floating text
                        FloatingTextProcessorSQL startText = new FloatingTextProcessorSQL(0);
                        startText.setKeyLetters("GD_CTXT");
                        startText.setModelElement(getModelElement());
                        // setup floating text
                        FloatingTextProcessorSQL middleText = new FloatingTextProcessorSQL(1);
                        middleText.setKeyLetters("GD_CTXT");
                        middleText.setModelElement(getModelElement());
                        // setup floating text
                        FloatingTextProcessorSQL endText = new FloatingTextProcessorSQL(2);
                        endText.setKeyLetters("GD_CTXT");
                        endText.setModelElement(getModelElement());
                        builder.append(SQLUtils.getInsertStatement(lineSegProcessorSQL, getModelElement()))
                                .append(SQLUtils.getInsertStatement(startWaypointProcessorSQL, getModelElement()))
                                .append(SQLUtils.getInsertStatement(endWayPointProcessorSQL, getModelElement()))
                                .append(startText.getProcessorOutput()).append(middleText.getProcessorOutput())
                                .append(endText.getProcessorOutput());
                    }
                }
            }
            GraphedgeProcessorSQL graphEdgeProcessor = new GraphedgeProcessorSQL(startId, endId);
            graphEdgeProcessor.setModelElement(getModelElement());
            graphEdgeProcessor.setKeyLetters("DIM_ED");
            return builder.append(SQLUtils.getInsertStatement(diagramelementProcessorSQL, getModelElement()))
                    .append(SQLUtils.getInsertStatement(graphelementProcessorSQL, getModelElement()))
                    .append(SQLUtils.getInsertStatement(connectorProcessorSQL, getModelElement()))
                    .append(SQLUtils.getInsertStatement(graphEdgeProcessor, getModelElement())).toString();
        }
    }

    public static ConnectorModel createConnector(String represents, String start, String end, String diagramId,
            int ooaType, float yOffset) {
        StringBuilder builder = new StringBuilder();
        String geId = IdProcessor.UUID().toString();
        GraphicalElement startGE = GraphicalElementProcessorSQL.createdGraphicalElements.get(start);
        GraphicalElement endGE = GraphicalElementProcessorSQL.createdGraphicalElements.get(end);
        GraphicalElementProcessorSQL connectionGE = new GraphicalElementProcessorSQL(geId, represents, diagramId,
                ooaType);
        connectionGE.setKeyLetters("GD_GE");
        DiagramelementProcessorSQL diagramelementProcessorSQL = new DiagramelementProcessorSQL(geId);
        diagramelementProcessorSQL.setKeyLetters("DIM_ELE");
        GraphelementProcessorSQL graphelementProcessorSQL = new GraphelementProcessorSQL(0f, 0f, geId);
        graphelementProcessorSQL.setKeyLetters("DIM_GE");
        ConnectorProcessorSQL connectorProcessorSQL = new ConnectorProcessorSQL(geId);
        connectorProcessorSQL.setKeyLetters("GD_CON");
        builder.append(connectionGE.getProcessorOutput()).append(diagramelementProcessorSQL.getProcessorOutput())
                .append(graphelementProcessorSQL.getProcessorOutput())
                .append(connectorProcessorSQL.getProcessorOutput());
        // create DIM_CON
        GraphconnectorProcessorSQL startCon = new GraphconnectorProcessorSQL(startGE.getId(),
                startGE.getRect().getMidPoint().x, startGE.getRect().getMidPoint().y);
        startCon.setKeyLetters("DIM_CON");
        if (end == null) {
            // whitespace
            // create line segment and waypoints
            WaypointProcessorSQL startWaypointProcessorSQL = new WaypointProcessorSQL(startCon.x, startCon.y,
                    IdProcessor.NULL_ID, geId);
            startWaypointProcessorSQL.setKeyLetters("DIM_WAY");
            WaypointProcessorSQL endWayPointProcessorSQL = new WaypointProcessorSQL(startCon.x, startCon.y + yOffset,
                    startWaypointProcessorSQL.getId(), geId);
            endWayPointProcessorSQL.setKeyLetters("DIM_WAY");
            LineSegmentProcessorSQL lineSegProcessorSQL = new LineSegmentProcessorSQL(geId,
                    startWaypointProcessorSQL.getId(), endWayPointProcessorSQL.getId());
            lineSegProcessorSQL.setKeyLetters("GD_LS");
            // if start is connector, create a GD_AOS
            if (startGE.isConnector()) {
                AnchorOnSegmentProcessorSQL aos = new AnchorOnSegmentProcessorSQL(startCon.getId(),
                        lineSegProcessorSQL.getId());
                aos.setKeyLetters("GD_AOS");
                builder.append(aos.getProcessorOutput());
            }
            // setup floating text
            FloatingTextProcessorSQL startText = new FloatingTextProcessorSQL(0, geId);
            startText.setKeyLetters("GD_CTXT");
            // setup floating text
            FloatingTextProcessorSQL middleText = new FloatingTextProcessorSQL(1, geId);
            middleText.setKeyLetters("GD_CTXT");
            // setup floating text
            FloatingTextProcessorSQL endText = new FloatingTextProcessorSQL(2, geId);
            endText.setKeyLetters("GD_CTXT");
            GraphedgeProcessorSQL graphEdgeProcessor = new GraphedgeProcessorSQL(geId, startCon.getId(), "");
            graphEdgeProcessor.setKeyLetters("DIM_ED");
            builder.append(graphEdgeProcessor.getProcessorOutput()).append(lineSegProcessorSQL.getProcessorOutput())
                    .append(startWaypointProcessorSQL.getProcessorOutput())
                    .append(endWayPointProcessorSQL.getProcessorOutput()).append(startText.getProcessorOutput())
                    .append(middleText.getProcessorOutput()).append(endText.getProcessorOutput());
            // add this graphic to created list
            createdGraphicalElements.put(represents, new GraphicalElement(geId, represents,
                    new Rectangle(startCon.x, startCon.y, 1F, (startCon.y + yOffset) - startCon.y), ooaType));
        } else {
            GraphconnectorProcessorSQL endCon = new GraphconnectorProcessorSQL(endGE.getId(),
                    endGE.getRect().getMidPoint().x, endGE.getRect().getMidPoint().y);
            endCon.setKeyLetters("DIM_CON");
            // create line segment and waypoints
            WaypointProcessorSQL startWaypointProcessorSQL = new WaypointProcessorSQL(startCon.x, startCon.y,
                    IdProcessor.NULL_ID, geId);
            startWaypointProcessorSQL.setKeyLetters("DIM_WAY");
            WaypointProcessorSQL endWayPointProcessorSQL = new WaypointProcessorSQL(endCon.x, endCon.y,
                    startWaypointProcessorSQL.getId(), geId);
            endWayPointProcessorSQL.setKeyLetters("DIM_WAY");
            LineSegmentProcessorSQL lineSegProcessorSQL = new LineSegmentProcessorSQL(geId,
                    startWaypointProcessorSQL.getId(), endWayPointProcessorSQL.getId());
            lineSegProcessorSQL.setKeyLetters("GD_LS");
            // if start is connector, create a GD_AOS
            if (startGE.isConnector()) {
                AnchorOnSegmentProcessorSQL aos = new AnchorOnSegmentProcessorSQL(startCon.getId(),
                        lineSegProcessorSQL.getId());
                aos.setKeyLetters("GD_AOS");
                builder.append(aos.getProcessorOutput());
            }
            // if end is connector, create a GD_AOS
            if (endGE.isConnector()) {
                AnchorOnSegmentProcessorSQL aos = new AnchorOnSegmentProcessorSQL(endCon.getId(),
                        lineSegProcessorSQL.getId());
                aos.setKeyLetters("GD_AOS");
                builder.append(aos.getProcessorOutput());
            }
            // setup floating text
            FloatingTextProcessorSQL startText = new FloatingTextProcessorSQL(0, geId);
            startText.setKeyLetters("GD_CTXT");
            // setup floating text
            FloatingTextProcessorSQL middleText = new FloatingTextProcessorSQL(1, geId);
            middleText.setKeyLetters("GD_CTXT");
            // setup floating text
            FloatingTextProcessorSQL endText = new FloatingTextProcessorSQL(2, geId);
            endText.setKeyLetters("GD_CTXT");
            GraphedgeProcessorSQL graphEdgeProcessor = new GraphedgeProcessorSQL(geId, startCon.getId(),
                    endCon.getId());
            graphEdgeProcessor.setKeyLetters("DIM_ED");
            builder.append(graphEdgeProcessor.getProcessorOutput()).append(lineSegProcessorSQL.getProcessorOutput())
                    .append(startWaypointProcessorSQL.getProcessorOutput())
                    .append(endWayPointProcessorSQL.getProcessorOutput()).append(startText.getProcessorOutput())
                    .append(middleText.getProcessorOutput()).append(endText.getProcessorOutput())
                    .append(endCon.getProcessorOutput());
            // add this graphic to created list
            createdGraphicalElements.put(represents, new GraphicalElement(geId, represents,
                    new Rectangle(startCon.x, startCon.y, endCon.x - startCon.x, endCon.y - startCon.y), ooaType));
        }
        return new ConnectorModel(builder.append(startCon.getProcessorOutput()).toString(), represents);
    }

    private Rectangle getBoundsFromString(String boundsString) {
        String[] attributes = boundsString.split(";");
        Map<String, String> values = new HashMap<>();
        Stream.of(attributes).forEach(a -> {
            String[] kv = a.split("=");
            if (kv.length == 2) {
                values.put(kv[0], kv[1]);
            }
        });
        Float x = values.get("Left") != null ? Float.valueOf(values.get("Left")) : 0f;
        Float y = values.get("Top") != null ? Float.valueOf(values.get("Top")) : 0f;
        Float right = values.get("Right") != null ? Float.valueOf(values.get("Right")) : 0f;
        Float bottom = values.get("Bottom") != null ? Float.valueOf(values.get("Bottom")) : 0f;
        Float w = right - x > 0 ? right - x : 0f;
        Float h = bottom - y > 0 ? bottom - y : 0f;
        return new Rectangle(x, y, w, h);
    }

    private ConnectorDescription getPolylineFromString(String boundsString) {
        String[] attributes = boundsString.split(";");
        List<Point> points = new ArrayList<>();
        Point start = new Point();
        Point end = new Point();
        Edge edge = Edge.Center;
        for (int i = 0; i < attributes.length; i++) {
            String attribute = attributes[i];
            if (attribute.contains("SX")) {
                float startX = Float.parseFloat(attribute.split("=")[1]);
                start.x = startX;
            }
            if (attribute.contains("SY")) {
                float startY = Float.parseFloat(attribute.split("=")[1]);
                start.y = startY;
            }
            if (attribute.contains("EX")) {
                float endX = Float.parseFloat(attribute.split("=")[1]);
                end.x = endX;
            }
            if (attribute.contains("EY")) {
                float endY = Float.parseFloat(attribute.split("=")[1]);
                end.y = endY;
            }
            if (attribute.contains("EDGE")) {
                int value = Integer.valueOf(attribute.split("=")[1]);
                switch (value) {
                    case 1:
                        edge = Edge.Top;
                        break;
                    case 2:
                        edge = Edge.Right;
                        break;
                    case 3:
                        edge = Edge.Bottom;
                        break;
                    case 4:
                        edge = Edge.Left;
                        break;
                    default:
                        break;
                }
            }
        }
        points.add(start);
        points.add(end);
        return new ConnectorDescription(new Polyline(points), edge);
    }

    public boolean isShape() {
        return isShape(getModelElement());
    }

    public boolean isShape(ModelElement element) {
        // create through another GD_GE, currently only connectors
        if (getModelElement() == null) {
            return false;
        }
        Rectangle bounds = getBoundsFromString(getModelElement().getPlainAttribute("bounds"));
        return bounds.w != 0 && bounds.h != 0;
    }

    private int getOoaTypeFromOoaId() {
        String representsId = getModelElement().getPlainAttribute("element");
        String keyLetters = IdProcessor.elementIdKeyLetts.get(UUID.fromString(IdProcessor.process(representsId, null)));
        ConnectionInformation information = ConnectionInformation.connectionMap
                .get(getModelElement().getPlainAttribute("element"));
        if (information != null) {
            // allow connection information to override
            if (information.getOoaType() != -1) {
                return information.getOoaType();
            }
        }
        if (keyLetters == null) {
            keyLetters = "";
        }
        switch (keyLetters) {
            case "C_C":
                return 98;
            case "C_I":
                return 96;
            case "O_OBJ":
                return 21;
            case "PE_C":
                return 210;
            case "S_UDT":
                return 51;
            case "IA_UCP":
                return 85;
            case "SQ_AP":
                return 65;
            case "S_EDT":
                return 52;
            case "SM_STATE":
                return 41;
            case "R_REL":
                return 24;
            case "R_SUB":
                // graphics created in class as subtype
                return 0;
            case "R_SUPER":
                // graphics created in class as subtype
                return 0;
            case "SM_TXN":
                return 42;
            case "UC_UCA":
                return 87;
            case "R_ASSR":
                // graphics created in class as link processor
                return 0;
            default:
                break;
        }
        return 0;
    }

    @Override
    public void postprocess(ModelElement element, String keyletters) {
        createdGraphicalElements.put(element.getPlainAttribute("element"),
                new GraphicalElement(element.getPlainAttribute("id"), element.getPlainAttribute("element"),
                        getBoundsFromString(element.getPlainAttribute("bounds")), getOoaTypeFromOoaId()));
        elementId = "";
        importedClassId = null;
        importedClassSQL = "";
        ooaType = null;
        diagramId = null;
        represents = null;
        skipSupporting = false;
    }

    @Override
    public IgnoreType ignoreTranslation() {
        // EA has model class elements on statecharts, that is not supported in xtuml
        String type = getModelElement().getOwner().getPlainAttribute("type");
        int ooaType = getOoaTypeFromOoaId();
        if (!type.equals("Logical") && ooaType == 21) {
            return IgnoreType.NOT_HANDLED;
        }
        return getOoaTypeFromOoaId() == 0 ? IgnoreType.NOT_HANDLED : IgnoreType.NOT_IGNORED;
    }

    @Override
    public List<String> getValues(ModelElement element) {
        return Stream
                .of(getelementId(), getdiagramId(), getOOA_ID(), getOOA_Type(), getrepresents(), getrepresents_path())
                .collect(Collectors.toList());
    }
}
