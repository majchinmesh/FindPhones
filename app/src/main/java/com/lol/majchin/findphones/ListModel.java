package com.lol.majchin.findphones;

/**
 * Created by majch on 08-10-2016.
 */

public class ListModel {

    private  String PhoneName="";
    private  String PhoneCompany="";
    private  String PhonePrice="";
    private String PhoneIcon = ""; // the encoded data of the icon image
    private int ADID = 0 ;
    
    /*********** Set Methods ******************/

    public void setPhoneName(String PhoneName)
    {
        this.PhoneName = PhoneName;
    }

    public void setPhoneCompany(String PhoneCompany)
    {
        this.PhoneCompany = PhoneCompany;
    }

    public void setPhonePrice(String PhonePrice)
    {
        this.PhonePrice = PhonePrice;
    }

    public void setPhoneIcon(String PhoneIcon)
    {
        this.PhoneIcon = PhoneIcon;
    }

    public void setADID(int aid )
    {
        this.ADID = aid ;
    }
    /*********** Get Methods ****************/

    public String getPhoneName()
    {
        return this.PhoneName;
    }

    public String getPhoneCompany()
    {
        return this.PhoneCompany;
    }

    public String getPhonePrice() {return this.PhonePrice;}

    public String getPhoneIcon() {return this.PhoneIcon;}

    public int getADID() {return this.ADID;}

}
