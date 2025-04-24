package bip.bip_project.model.device;

import java.util.Date;

public class DeviceResponseDto {
    private Integer id;

    private String deviceType;

    private String model;

    private String manufacturer;

    private Date dateOfCreation;

    private String imageUrl;

    public DeviceResponseDto(){}

    public DeviceResponseDto(Integer id, String deviceType, String model, String manufacturer, Date dateOfCreation, String imageUrl) {
        this.id = id;
        this.deviceType = deviceType;
        this.model = model;
        this.manufacturer = manufacturer;
        this.dateOfCreation = dateOfCreation;
        this.imageUrl = imageUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }
}
