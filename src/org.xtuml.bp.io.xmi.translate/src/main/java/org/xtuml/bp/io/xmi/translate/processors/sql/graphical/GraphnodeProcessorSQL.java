package org.xtuml.bp.io.xmi.translate.processors.sql.graphical;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sdmetrics.model.ModelElement;

import org.xtuml.bp.io.xmi.translate.processors.generated.AbstractGraphnodeProcessor;
import org.xtuml.bp.io.xmi.translate.processors.sql.SQLUtils;

public class GraphnodeProcessorSQL extends AbstractGraphnodeProcessor {

    private Float width = 100f;
    private Float height = 100f;
    private String Id;

    public GraphnodeProcessorSQL(Float width, Float height, String Id) {
        this.width = width;
        this.height = height;
        this.Id = Id;
    }

    @Override
    public String getwidth() {
        return SQLUtils.numberValue(width);
    }

    @Override
    public String getheight() {
        return SQLUtils.numberValue(height);
    }

    @Override
    public String getelementId() {
        if (Id != null) {
            return SQLUtils.idValue(Id);
        }
        return SQLUtils.idValue(getModelElement().getPlainAttribute("id"));
    }

    @Override
    public String getProcessorOutput() {
        return SQLUtils.getProcessorOutput(this);
    }

    @Override
    public List<String> getValues(ModelElement element) {
        return Stream.of(getwidth(), getheight(), getelementId()).collect(Collectors.toList());
    }
}
