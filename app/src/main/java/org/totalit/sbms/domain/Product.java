package org.totalit.sbms.domain;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Product {


    @Expose
    private int id;
    @Expose
    private Long categoryId;

    @Expose
    private Long companyId;

    @Expose
    private String model;
    @Expose
    private String brand;
    @Expose
    private String productCode;
    @Expose
    private String warranty;
    @Expose
    private String monthYear;
    @Expose
    private String description;
    @Expose
    private String quantityDelivered;
    @Expose
    private Long availableStock;
    @Expose
    private Double landingCost;
    @Expose
    private Float factor;
    @Expose
    private Double recommendedPrice;
    @Expose
    private Double sellingPrice;
    @Expose
    private Double retailPrice;
    @Expose
    private Double unitPrice;
    @Expose
    private Long reOrderLevel;
    @Expose
    private Long reOrderQuantity;
    @Expose
    private String serialNumber;
    @Expose
    private List<Supplier> suppliers = new ArrayList<>();
    /**
     * Product Specifications
     */
    @Expose
    private String display;
    @Expose
    private String processor;
    @Expose
    private String memory;
    @Expose
    private String hardDrive;
    @Expose
    private String os;
    @Expose
    private String compatibility;
    @Expose
    private String monitorSize;
    @Expose
    private String resolution;
    @Expose
    private String videoInput;
    @Expose
    private String catridge;
    @Expose
    private String color;
    @Expose
    private String dutyCycle;
    @Expose
    private String duplex;
    @Expose
    private String scanner;
    @Expose
    private String ethernet;
    @Expose
    private String wireless;
    @Expose
    private String fax;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantityDelivered() {
        return quantityDelivered;
    }

    public void setQuantityDelivered(String quantityDelivered) {
        this.quantityDelivered = quantityDelivered;
    }

    public Long getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Long availableStock) {
        this.availableStock = availableStock;
    }

    public Double getLandingCost() {
        return landingCost;
    }

    public void setLandingCost(Double landingCost) {
        this.landingCost = landingCost;
    }

    public Float getFactor() {
        return factor;
    }

    public void setFactor(Float factor) {
        this.factor = factor;
    }

    public Double getRecommendedPrice() {
        return recommendedPrice;
    }

    public void setRecommendedPrice(Double recommendedPrice) {
        this.recommendedPrice = recommendedPrice;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getReOrderLevel() {
        return reOrderLevel;
    }

    public void setReOrderLevel(Long reOrderLevel) {
        this.reOrderLevel = reOrderLevel;
    }

    public Long getReOrderQuantity() {
        return reOrderQuantity;
    }

    public void setReOrderQuantity(Long reOrderQuantity) {
        this.reOrderQuantity = reOrderQuantity;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getHardDrive() {
        return hardDrive;
    }

    public void setHardDrive(String hardDrive) {
        this.hardDrive = hardDrive;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getCompatibility() {
        return compatibility;
    }

    public void setCompatibility(String compatibility) {
        this.compatibility = compatibility;
    }

    public String getMonitorSize() {
        return monitorSize;
    }

    public void setMonitorSize(String monitorSize) {
        this.monitorSize = monitorSize;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getVideoInput() {
        return videoInput;
    }

    public void setVideoInput(String videoInput) {
        this.videoInput = videoInput;
    }

    public String getCatridge() {
        return catridge;
    }

    public void setCatridge(String catridge) {
        this.catridge = catridge;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDutyCycle() {
        return dutyCycle;
    }

    public void setDutyCycle(String dutyCycle) {
        this.dutyCycle = dutyCycle;
    }

    public String getDuplex() {
        return duplex;
    }

    public void setDuplex(String duplex) {
        this.duplex = duplex;
    }

    public String getScanner() {
        return scanner;
    }

    public void setScanner(String scanner) {
        this.scanner = scanner;
    }

    public String getEthernet() {
        return ethernet;
    }

    public void setEthernet(String ethernet) {
        this.ethernet = ethernet;
    }

    public String getWireless() {
        return wireless;
    }

    public void setWireless(String wireless) {
        this.wireless = wireless;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
