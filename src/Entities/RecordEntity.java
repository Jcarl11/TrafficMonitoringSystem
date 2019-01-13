
package Entities;

public class RecordEntity 
{
    private String time;
    private String facility;
    private String facilityType;
    private String levelOfService;  
    
    public String getTime() {return time;}
    public void setTime(String time) {this.time = time;}
    public String getFacility() {return facility;}
    public void setFacility(String facility) {this.facility = facility;}
    public String getFacilityType() {return facilityType;}
    public void setFacilityType(String facilityType) {this.facilityType = facilityType;}
    public String getLevelOfService() {return levelOfService;}
    public void setLevelOfService(String levelOfService) {this.levelOfService = levelOfService;}
    
    
    public RecordEntity(String time, String facility, String facilityType, String levelOfService) 
    {
        this.time = time;
        this.facility = facility;
        this.facilityType = facilityType;
        this.levelOfService = levelOfService;
    }
    public RecordEntity(){}
    
}
