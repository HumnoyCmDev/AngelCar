package com.beta.cls.angelcar.gao;

import com.beta.cls.angelcar.util.LineUp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by humnoy on 10/3/59.
 */
@Parcel
public class PostCarGao {
    @SerializedName("carid")            @Expose int carId;
    @SerializedName("shopref")          @Expose String shopRef;
    @SerializedName("carname")          @Expose String carName;
    @SerializedName("cardetail")        @Expose String carDetail;
    @SerializedName("caryear")          @Expose int carYear;
    @SerializedName("carprice")         @Expose String carPrice;
    @SerializedName("carstatus")        @Expose String carStatus;
    @SerializedName("gear")             @Expose int gear;
    @SerializedName("plate")            @Expose String plate;
    @SerializedName("name")             @Expose String name;
    @SerializedName("province_name")    @Expose String provinceName;
    @SerializedName("carimagepath")     @Expose String carImagePath;

    public String getCarImagePath() {
        return carImagePath;
    }

    public void setCarImagePath(String carImagePath) {
        this.carImagePath = carImagePath;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getShopRef() {
        return shopRef;
    }

    public void setShopRef(String shopRef) {
        this.shopRef = shopRef;
    }

    public String getCarDetail() {
        return carDetail;
    }

    public void setCarDetail(String carDetail) {
        this.carDetail = carDetail;
    }

    public int getCarYear() {
        return carYear;
    }

    public void setCarYear(int carYear) {
        this.carYear = carYear;
    }

    public String getCarPrice() {
        return carPrice;
    }

    public void setCarPrice(String carPrice) {
        this.carPrice = carPrice;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }


    public int getGear() {
        return gear;
    }

    public void setGear(int gear) {
        this.gear = gear;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String toMessage(){
        String titleCar = String.format("<p><b><u><i><h3>%s</h3></i></u></b><p>",getCarName());
        String topic = String.format("<p><b>%s</b></p><p>%s<b>",
                LineUp.getInstance().subTopic(getCarDetail()),LineUp.getInstance().subDetail(getCarDetail()));

        String detail = String.format(
                "<p>" +
                "<b>ราคา.</b> %s" + //"<b>ราคา.</b> %s " +
                "</p>",getCarPrice());

        String message = titleCar+topic+detail;
        return String.format("<header>%s</header>",message);
    }
}
