package tsi.dm.gcpd.atividadejokenpo;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Winner implements Parcelable, Comparable<Winner> {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private Long id;
    private Long startTime;
    private Long endTime;
    private Integer score;
    private Integer isPlayer;
    private String nome;
    private Integer isLast;

    public Winner(){
        this.startTime = System.currentTimeMillis();
        this.nome = "Bot";
    }

    @SuppressLint("NewApi")
    protected Winner(Parcel in) {
        this.startTime = in.readLong();
        this.endTime = in.readLong();
        this.score = in.readInt();
        this.isPlayer = in.readInt();
        this.nome = in.readString();
        this.isLast = in.readInt();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.startTime);
        dest.writeLong(this.endTime);
        dest.writeInt(this.score);
        dest.writeInt(this.isPlayer);
        dest.writeString(this.nome);
        dest.writeInt(this.isLast);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Winner> CREATOR = new Creator<Winner>() {
        @Override
        public Winner createFromParcel(Parcel in) {
            return new Winner(in);
        }

        @Override
        public Winner[] newArray(int size) {
            return new Winner[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer isPlayer() {
        return isPlayer;
    }

    public void setIsPlayer(Integer isPlayer) {
        this.isPlayer = isPlayer;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIsLast() {
        return isLast;
    }

    public void setIsLast(Integer isLast) {
        this.isLast = isLast;
    }

    @Override
    public int compareTo(Winner other) {
        int last = this.score.compareTo(other.score);
        if(last == 0){
            Long difThis = this.endTime - this.startTime;
            Long difOther = other.endTime - other.startTime;
            return -1*(difThis.compareTo(difOther));
        }
        return last;
    }

    @Override
    public String toString() {
        return nome + " - " + "Pontuação: " + score;
    }
}
