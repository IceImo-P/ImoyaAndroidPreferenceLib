package net.imoya.android.preference.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

/**
 * 時:分によって指定される時刻モデル
 */
public class Time implements Parcelable {
    /**
     * 時
     */
    private int hour;
    /**
     * 分
     */
    private int minute;

    /**
     * 文字列を解析して、対応する {@link Time} を返します。
     * 文字列の書式は&quot;&lt;Hour&gt;:&lt;Minute&gt;&quot;です。<ul>
     * <li>各項目の省略はできません。</li>
     * <li>Hourは0～23、Minuteは0～59の整数値を、10進数で表記する必要があります。</li>
     * </ul>
     *
     * @param s 文字列
     * @return {@link Time}
     * @throws NumberFormatException 引数 s が null です
     * @throws IllegalArgumentException 引数 s の書式が不正です。
     */
    public static Time parse(String s) throws NullPointerException, IllegalArgumentException {
        if (s == null) {
            throw new NullPointerException("s == null");
        }
        // コロンを探す
        final int separatorPos = s.indexOf(':');
        if (separatorPos == -1) {
            throw new IllegalArgumentException("':' not found");
        } else if (separatorPos == 0) {
            throw new IllegalArgumentException("Hour not found");
        } else if (separatorPos == s.length() - 1) {
            throw new IllegalArgumentException("Minute not found");
        }
        // コロンで区切る
        final String hour = s.substring(0, separatorPos);
        final String minute = s.substring(separatorPos + 1);

        // 時:分を取得し、フィールドへ保存する
        final Time result = new Time();
        try {
            result.hour = Integer.valueOf(hour, 10);
            result.minute = Integer.valueOf(minute, 10);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
        if (result.hour < 0 || result.hour > 23 || result.minute < 0 || result.minute > 59) {
            throw new IllegalArgumentException("Illegal number");
        }
        return result;
    }

    public Time() {
        this.hour = 0;
        this.minute = 0;
    }

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    private Time(Parcel parcel) {
        this.hour = parcel.readByte();
        this.minute = parcel.readByte();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte)this.hour);
        dest.writeByte((byte)this.minute);
    }

    public int getHour() {
        return this.hour;
    }

    public void setHour(int hour) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("hour requires 0 - 23");
        }
        this.hour = hour;
    }

    public int getMinute() {
        return this.minute;
    }

    public void setMinute(int minute) {
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("minute requires 0 - 59");
        }
        this.minute = minute;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Time)) {
            return false;
        }
        final Time another = (Time) obj;
        return this.hour == another.hour
                && this.minute == another.minute;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%d:%02d", this.hour, this.minute);
    }

    @Override
    public int hashCode() {
        return this.hour * 60 + this.minute;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * {@link Parcelable} 対応用 {@link Creator}
     */
    public static final Creator<Time> CREATOR = new Creator<Time>() {
        /**
         * {@link Parcel} の内容を保持するオブジェクトを生成して返します。
         *
         * @param parcel {@link Parcel}
         * @return {@link Parcel} の内容を保持するオブジェクト
         */
        @Override
        public Time createFromParcel(Parcel parcel) {
            return new Time(parcel);
        }

        /**
         * オブジェクトの配列を生成して返します。
         *
         * @param size 配列のサイズ
         * @return 配列
         */
        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };
}
