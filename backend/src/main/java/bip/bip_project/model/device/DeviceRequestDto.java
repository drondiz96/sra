package bip.bip_project.model.device;

import java.time.LocalDate;
import java.util.Date;

public class DeviceRequestDto {
    private String deviceType;

    private String model;

    private String manufacturer;

    private LocalDate dateOfCreation;

    private String imageUrl;

    public DeviceRequestDto(){}

    public DeviceRequestDto(String deviceType, String model, String manufacturer, LocalDate dateOfCreation, String imageUrl) {
        this.deviceType = deviceType;
        this.model = model;
        this.manufacturer = manufacturer;
        this.dateOfCreation = dateOfCreation;
        this.imageUrl = imageUrl;
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

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDate dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }
}
