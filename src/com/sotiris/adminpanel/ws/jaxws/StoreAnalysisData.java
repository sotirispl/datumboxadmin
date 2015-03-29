
package com.sotiris.adminpanel.ws.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "storeAnalysisData", namespace = "http://ws.adminpanel.sotiris.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "storeAnalysisData", namespace = "http://ws.adminpanel.sotiris.com/")
public class StoreAnalysisData {

    @XmlElement(name = "arg0", namespace = "")
    private com.sotiris.adminpanel.models.Analysis arg0;

    /**
     * 
     * @return
     *     returns Analysis
     */
    public com.sotiris.adminpanel.models.Analysis getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(com.sotiris.adminpanel.models.Analysis arg0) {
        this.arg0 = arg0;
    }

}
