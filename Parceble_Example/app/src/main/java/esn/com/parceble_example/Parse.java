package esn.com.parceble_example;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by natraj.gadumala on 6/16/2015.
 */
public class Parse implements Parcelable {

    String s1,s2;
    int n1,n2;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(s1);
        dest.writeString(s2);
        dest.writeInt(n1);
        dest.writeInt(n2);
    }

    public Parse(String s1,String s2,int n1,int n2){
        this.s1=s1;
        this.s2=s2;
        this.n1=n1;
        this.n2=n2;

    }
    private Parse(Parcel in){
        this.s1 = in.readString();
        this.s2 = in.readString();
        this.n1 = in.readInt();
        this.n2 = in.readInt();
    }
    public final static Parcelable.Creator<Parse> CREATOR= new Creator<Parse>() {
        @Override
        public Parse createFromParcel(Parcel source) {
            return new Parse(source);
        }

        @Override
        public Parse[] newArray(int size) {
            return new Parse[size];
        }
    };

}
