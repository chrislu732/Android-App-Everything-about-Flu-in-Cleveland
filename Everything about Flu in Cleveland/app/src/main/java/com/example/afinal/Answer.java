package com.example.afinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Answer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        TextView test = findViewById(R.id.text5);
        test.setText("Q．Which of the following are the symptoms of the flu"+"\n"+
                "TA: Fever over 102F, headache, cough, Extreme Exhaustion"+"\n"
        +"P: Common flu symptoms include Fever over 102°F, stuffy nose, nausea, chills and sweats, " +
                "feeling tired, muscle aches, cough, headache, poor appetite."+"\n\n"+

                "Q. Should pregnant women get vaccinated?"+"\n"
        +"TA: yes"+"\n"+
        "P: Pregnant women should get vaccinated, no matter what stage of pregnancy they’re in. " + "\n" +
                        "If you’re pregnant, changes in your heart, lungs, and immune system make the " +"\n"+
                        "symptoms of the flu more dangerous for you and your developing fetus.+"+"\n"+
                "Among other concerns, getting the flu raises your risk of premature labor and delivery. " +"\n"+
                "Getting the flu shot will help protect you and your unborn baby, even after birth."+"\n"+
        "If you’re concerned about thimerosal, a mercury-based preservative used in flu vaccines, " +"\n" +
                "you can request a preservative-free vaccine."+"\n\n"

                +"Q. Can children younger than 6 months of age get a flu shot?"+"\n"
                +"TA: no"+"\n"
        +"P: Children under 6 months old, people who are very allergic to the flu shot " + "\n"+
                        "or any of its ingredients and people with a history of Guillain-Barre syndrome (GBS), " +"\n"+
                        "which is a disorder that causes weakness and paralysis can’t get a flu shot."+"\n\n"

                +"Q. When is the best time to get a flu shot in U.S.?"+"\n"
                +"TA: October to March"+"\n"+
        "P: Exactly when the flu season starts and ends is unpredictable, " +"\n" +
                "so health officials recommend that people get their flu shot in " + "\n" +
                        "early fall, preferably by the end of October, the CDC says. Flu activity typically " +"\n"+
                                "peaks in January or February."+"\n\n"+

        "Q. Can the vaccine give me the flu?"+"\n"
        +"TA: no"+"\n"
        +"P:The flu vaccine can't give you the flu. But you might develop flu-like symptoms — despite getting a flu vaccine — for a variety of reasons, including: Reaction to the vaccine, the two-week window, mismatched flu viruses and other illnesses");
    }
}
