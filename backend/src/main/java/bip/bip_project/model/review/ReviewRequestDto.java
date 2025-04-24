package bip.bip_project.model.review;

import bip.bip_project.model.device.Device;
import bip.bip_project.model.device.DeviceRequestDto;

public class ReviewRequestDto {

    Integer id;

    String title;

    String content;

    DeviceRequestDto device;

    public ReviewRequestDto(){}

    public ReviewRequestDto(String title, String content, DeviceRequestDto device, Integer id) {
        this.title = title;
        this.content = content;
        this.device = device;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DeviceRequestDto getDevice() {
        return device;
    }

    public void setDevice(DeviceRequestDto device) {
        this.device = device;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
