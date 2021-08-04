package net.imoya.android.preference.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

/**
 * 時:分によって指定される両端を持つ期間モデル
 */
public class TimePeriod implements Parcelable {
    /**
     * 開始時
     */
    private int startHour = 0;
    /**
     * 開始分
     */
    private int startMinute = 0;
    /**
     * 終了時
     */
    private int endHour = 0;
    /**
     * 終了分
     */
    private int endMinute = 0;

    /**
     * 文字列を解析して、対応する {@link TimePeriod} を返します。
     * 文字列の書式は
     * &quot;&lt;StartHour&gt;:&lt;StartMinute&gt;-&lt;EndHour&gt;:&lt;EndMinute&gt;&quot; です。<ul>
     * <li>各項目の省略はできません。</li>
     * <li>Hourは0～23、Minuteは0～59の整数値を、10進数で表記する必要があります。</li>
     * <li>StartとEndの大小関係はチェックされません。</li>
     * </ul>
     *
     * @param s 文字列
     * @return {@link TimePeriod}
     * @throws NumberFormatException    引数 s が null です
     * @throws IllegalArgumentException 引数 s の書式が不正です。
     */
    public static TimePeriod parse(String s) throws NullPointerException, IllegalArgumentException {
        if (s == null) {
            throw new NullPointerException("s == null");
        }
        // ハイフンの位置を探す
        final int separatorPos = s.indexOf('-');
        if (separatorPos == -1) {
            throw new IllegalArgumentException("'-' not found");
        } else if (separatorPos == 0) {
            throw new IllegalArgumentException("Start time not found");
        } else if (separatorPos == s.length() - 1) {
            throw new IllegalArgumentException("End time not found");
        }
        // ハイフンで区切る
        final String start = s.substring(0, separatorPos);
        final String end = s.substring(separatorPos + 1);

        // それぞれの時:分を取得し、フィールドへ保存する
        final Time startTime = Time.parse(start);
        final Time endTime = Time.parse(end);
        final TimePeriod result = new TimePeriod();
        result.startHour = startTime.getHour();
        result.startMinute = startTime.getMinute();
        result.endHour = endTime.getHour();
        result.endMinute = endTime.getMinute();
        return result;
    }

    public TimePeriod() {
    }

//    public TimePeriod(TimePeriod source) {
//        this.copyFrom(source);
//    }

    private TimePeriod(Parcel parcel) {
        this.startHour = parcel.readByte();
        this.startMinute = parcel.readByte();
        this.endHour = parcel.readByte();
        this.endMinute = parcel.readByte();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) this.startHour);
        dest.writeByte((byte) this.startMinute);
        dest.writeByte((byte) this.endHour);
        dest.writeByte((byte) this.endMinute);
    }

    public void copyFrom(TimePeriod source) {
        if (source == null)
            throw new IllegalArgumentException("source == null");
        this.startHour = source.startHour;
        this.startMinute = source.startMinute;
        this.endHour = source.endHour;
        this.endMinute = source.endMinute;
    }

    public int getStartHour() {
        return this.startHour;
    }

    public void setStartHour(int startHour) {
        if (startHour < 0 || startHour > 23) {
            throw new IllegalArgumentException("startHour requires 0 - 23");
        }
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return this.startMinute;
    }

    public void setStartMinute(int startMinute) {
        if (startMinute < 0 || startMinute > 59) {
            throw new IllegalArgumentException("startMinute requires 0 - 59");
        }
        this.startMinute = startMinute;
    }

    public int getEndHour() {
        return this.endHour;
    }

    public void setEndHour(int endHour) {
        if (endHour < 0 || endHour > 23) {
            throw new IllegalArgumentException("endHour requires 0 - 23");
        }
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return this.endMinute;
    }

    public void setEndMinute(int endMinute) {
        if (endMinute < 0 || endMinute > 59) {
            throw new IllegalArgumentException("endMinute requires 0 - 59");
        }
        this.endMinute = endMinute;
    }

    /**
     * 指定の時刻が、範囲内であるか否かを返します。
     *
     * @param hourOfDay 時(0～23)、又は未定義を表す-1
     * @param minute    分(0～59)、又は未定義を表す-1
     * @return 指定の時刻が範囲内の場合はtrue, 範囲外であるか未定義である場合はfalse
     */
    public boolean isInPeriod(int hourOfDay, int minute) {
        if (hourOfDay > 23 || minute > 59) {
            throw new IllegalArgumentException("Illegal time(" + hourOfDay + ":" + minute + ")");
        }
        if (this.startHour < 0 || this.startMinute < 0
                || this.endHour < 0 || this.endMinute < 0) {
            // 未定義の場合は、常に範囲外とする。
            return false;
        }

        final int target = hourOfDay * 60 + minute;
        final int from = this.startHour * 60 + this.startMinute;
        final int to = this.endHour * 60 + this.endMinute;

        // from <= to ならばその間がこのインスタンスが表す範囲である。
        // from > to ならばその間以外がこのインスタンスが表す範囲である。
        if (from <= to) {
            if (from <= target) {
                return target <= to;
            }
        } else {
            if (from <= target) {
                return true;
            }
            return target <= to;
        }

        // 以上に該当しなかったら、このインスタンスが表す範囲の中ではない。
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TimePeriod)) {
            return false;
        }
        final TimePeriod another = (TimePeriod) obj;
        return this.startHour == another.startHour
                && this.startMinute == another.startMinute
                && this.endHour == another.endHour
                && this.endMinute == another.endMinute;
    }

    /**
     * オブジェクトの文字列形式を得ます。
     * {@link #parse(String)} メソッドが解析可能な書式を出力します。
     *
     * @return 文字列形式
     */
    @Override
    public String toString() {
        return String.format(Locale.US, "%d:%02d-%d:%02d",
                this.startHour, this.startMinute, this.endHour, this.endMinute);
    }

    @Override
    public int hashCode() {
        return this.startHour * 86400 + this.startMinute * 1440
                + this.endHour * 60 + this.endMinute;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * {@link Parcelable} 対応用 {@link Creator}
     */
    public static final Creator<TimePeriod> CREATOR = new Creator<TimePeriod>() {
        /**
         * {@link Parcel} の内容を保持するオブジェクトを生成して返します。
         *
         * @param parcel {@link Parcel}
         * @return {@link Parcel} の内容を保持するオブジェクト
         */
        @Override
        public TimePeriod createFromParcel(Parcel parcel) {
            return new TimePeriod(parcel);
        }

        /**
         * オブジェクトの配列を生成して返します。
         *
         * @param size 配列のサイズ
         * @return 配列
         */
        @Override
        public TimePeriod[] newArray(int size) {
            return new TimePeriod[size];
        }
    };
}
