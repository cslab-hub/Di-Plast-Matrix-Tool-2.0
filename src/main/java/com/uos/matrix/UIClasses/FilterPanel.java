/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.UIClasses;

import javax.faces.component.FacesComponent;
import javax.faces.component.ValueHolder;
import javax.faces.convert.Converter;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import org.primefaces.component.overlaypanel.OverlayPanel;

/**
 *
 * 
 */
@FacesComponent(
    value = FilterPanel.COMPONENT_TYPE,
    createTag = true,
    tagName = FilterPanel.TAG_NAME,
    namespace = FilterPanel.NAMESPACE
)
/**                        <!--  <my:sliderV id="mfiSlider"  for="valMFI1,valMFI2"  display="displayRange" minValue="#{sellerView.MFIMin}" maxValue="#{sellerView.MFIMax}" step="0.1" range="true" displayTemplate="Between {min} and {max}" >
                            <p:ajax event="slideEnd" process="@this,valMFI1,valMFI2" oncomplete="PF('recycledTable').filter()"/>
                        </my:sliderV> 

                    <f:facet name="filter">
                        <cc:valueRange min="#{sellerView.MFIMin}" max="#{sellerView.MFIMax}" step="0.1" onComplete="PF('recycledTable').filter()" decreaseMinAction="#{sellerView.decreaseMFI1()}" increaseMaxAction="#{sellerView.increaseMFI2()}" decreaseMaxAction="#{sellerView.decreaseMFI2()}" increaseMinAction="#{sellerView.increaseMFI1()}" value1="#{sellerView.currentRequest.MFI}" >
                        </cc:valueRange>
                    </f:facet>

-->**/
public class FilterPanel extends OverlayPanel implements ValueHolder {
    
    public static final String TAG_NAME = "filterPanel";
    public static final String COMPONENT_TYPE = "my.domain.filterPanel";
    public static final String NAMESPACE = "http://xmlns.domain.my/jsf/component";
    
    public FilterPanel() {
        super();
    }
    
    
    @Override
    public Object getLocalValue() {
        return null;
    }
    
    
    public void setLocalValue(Object value){
        //
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object o) {
        //
    }

    @Override
    public Converter getConverter() {
        return null;
    }

    @Override
    public void setConverter(Converter cnvrtr) {
        //
    }
    
}
