/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.UIClasses;

import cern.colt.Arrays;
import java.util.Collections;
import javax.faces.component.FacesComponent;
import javax.faces.component.ValueHolder;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import org.primefaces.component.slider.Slider;

@FacesComponent(
    value = SliderValue.COMPONENT_TYPE,
    createTag = true,
    tagName = SliderValue.TAG_NAME,
    namespace = SliderValue.NAMESPACE
)
@ListenerFor(systemEventClass=PostAddToViewEvent.class)
public class SliderValue extends Slider implements ValueHolder {
    
    public static final String TAG_NAME = "sliderV";
    public static final String COMPONENT_TYPE = "my.domain.sliderV";
    public static final String NAMESPACE = "http://xmlns.domain.my/jsf/component";
    
    public SliderValue() {
        super();
    }
    
    
    @Override
    public Object getLocalValue() {
        return this.getMinValue()+ " ± " + this.getMaxValue();
    }
    
    
    public void setLocalValue(Object value){
        String sValue = value.toString();
        if(sValue.contains("-")){
            String[] ssValue = sValue.split(" ± ");
            setMinValue(Double.parseDouble(ssValue[0]));
            setMaxValue(Double.parseDouble(ssValue[1]));
        } else {
            setMinValue(Double.parseDouble(sValue));
        }
    }
}
