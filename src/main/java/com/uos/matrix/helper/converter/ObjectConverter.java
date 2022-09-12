package com.uos.matrix.helper.converter;

import com.uos.matrix.services.RequestService;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * The Class SelectOneMenuConverter.
 */
@FacesConverter("objectConverter")
public class ObjectConverter implements Converter {

    @Override
    public Object getAsObject(final FacesContext arg0, final UIComponent arg1, final String objectString) {
        if (objectString == null) {
            return null;
        }else if (objectString.isBlank()) {
            return null;
        }
        return fromSelect(arg1, objectString);
    }

    /**
     * Serialize.
     *
     * @param object the object
     * @return the string
     */
    private String serialize(final Object object) {
        if (object == null || object.toString() == null) {
            return "";
        } else if (object.toString().isBlank()) {
            return "";
        }
        return object.toString();
        //return object.getClass() + "@" + object.hashCode();
    }

    /**
     * From select.
     *
     * @param currentcomponent the currentcomponent
     * @param objectString the object string
     * @return the object
     */
    private Object fromSelect(final UIComponent currentcomponent, final String objectString) {

        if (currentcomponent.getClass() == UISelectItem.class) {
            final UISelectItem item = (UISelectItem) currentcomponent;
            final Object value = item.getValue();
            if (objectString.equals(serialize(value))) {
                return value;
            }
        }

        if (currentcomponent.getClass() == UISelectItems.class) {
            final UISelectItems items = (UISelectItems) currentcomponent;
            final List<Object> elements = (List<Object>) items.getValue();
            for (final Object element : elements) {
                if (objectString.equals(serialize(element))) {
                    return element;
                }
            }
        }

        if (!currentcomponent.getChildren().isEmpty()) {
            for (final UIComponent component : currentcomponent.getChildren()) {
                final Object result = fromSelect(component, objectString);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(final FacesContext arg0, final UIComponent arg1, final Object object) {
        return serialize(object);
    }

}
