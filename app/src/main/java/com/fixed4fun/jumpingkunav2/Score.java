package com.fixed4fun.jumpingkunav2;


import android.os.Parcel;
import android.os.Parcelable;

public class Score implements Parcelable {
    long t;
    int s;
    String n;

    protected Score(Parcel in) {
        t = in.readLong();
        s = in.readInt();
        n = in.readString();
    }


    public Score(long time, int score) {
        this.t = time;
        this.s = score;
    }

    public Score() {
    }

    public static final Creator<Score> CREATOR = new Creator<Score>() {
        @Override
        public Score createFromParcel(Parcel in) {
            return new Score(in);
        }

        @Override
        public Score[] newArray(int size) {
            return new Score[size];
        }
    };

    public long getT() {
        return t;
    }

    public void setT(long t) {
        this.t = t;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public String getName() {
        return n;
    }

    public void setName(String name) {
        this.n = name;
    }


    public Score(String name, long time, int score) {
        this.t = time;
        this.s = score;
        this.n = name;
    }

    @Override
    public String toString() {
        return "Score{" +
                " n=" + n +
                ", t=" + t +
                ", s=" + s +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(t);
        parcel.writeInt(s);
        parcel.writeString(n);
    }
}