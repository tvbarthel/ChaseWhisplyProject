package fr.tvbarthel.games.chasewhisply.model.bonus;


import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformationStandard;

public interface Bonus extends Parcelable {
    public void apply(GameInformationStandard gameInformation);

    public static class DummyBonus implements Bonus {

        public DummyBonus(Parcel in) {
        }

        public DummyBonus() {
        }

        @Override
        public void apply(GameInformationStandard gameInformation) {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }

        public static final Parcelable.Creator<DummyBonus> CREATOR = new Parcelable.Creator<DummyBonus>() {
            public DummyBonus createFromParcel(Parcel in) {
                return new DummyBonus(in);
            }

            public DummyBonus[] newArray(int size) {
                return new DummyBonus[size];
            }
        };
    }
}
