/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.UIClasses;


import javax.annotation.PostConstruct;
import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.component.ValueHolder;

@FacesComponent("valueRange")
public class ValueRangeComponent extends UIInput implements NamingContainer, ValueHolder {
    
    @PostConstruct
    public void init() {
        getAttributes().put("value1", getValue());
    }

    @Override
    public String getFamily() {
        return UINamingContainer.COMPONENT_FAMILY;
    }

    @Override
    public Object getLocalValue() {
       return getAttributes().get("value1");
    }
    
    

    /**@Override
    public Object getValue() {
        return getAttributes().get("value1");
        
    }*/
    
    

}
