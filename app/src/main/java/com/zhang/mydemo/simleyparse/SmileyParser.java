/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zhang.mydemo.simleyparse;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.zhang.mydemo.R;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class for annotating a CharSequence with spans to convert textual emoticons
 * to graphical ones.
 */
public class SmileyParser {
    // Singleton stuff
    private static SmileyParser sInstance;
    public static SmileyParser getInstance() { return sInstance; }
    public static void init(Context context) {
        sInstance = new SmileyParser(context);
    }

    private final Context mContext;
    private final String[] mSmileyTexts;
    private final Pattern mPattern;
    private final HashMap<String, Integer> mSmileyToRes;

    private SmileyParser(Context context) {
        mContext = context;
        mSmileyTexts = mContext.getResources().getStringArray(DEFAULT_SMILEY_TEXTS);
        mSmileyToRes = buildSmileyToRes();
        mPattern = buildPattern();
    }

    static class Smileys {
        private static final int[] sIconIds = {
//            R.mipmap.emo_im_happy,
//            R.mipmap.emo_im_sad,
//            R.mipmap.emo_im_winking,
//            R.mipmap.emo_im_tongue_sticking_out,
//            R.mipmap.emo_im_surprised,
//            R.mipmap.emo_im_kissing,
//            R.mipmap.emo_im_yelling,
//            R.mipmap.emo_im_cool,
//            R.mipmap.emo_im_money_mouth,
//            R.mipmap.emo_im_foot_in_mouth,
//            R.mipmap.emo_im_embarrassed,
//            R.mipmap.emo_im_angel,
//            R.mipmap.emo_im_undecided,
//            R.mipmap.emo_im_crying,
//            R.mipmap.emo_im_lips_are_sealed,
//            R.mipmap.emo_im_laughing,
//            R.mipmap.emo_im_wtf,
//            R.mipmap.emo_im_heart,
//            R.mipmap.emo_im_mad,
//            R.mipmap.emo_im_smirk,
//            R.mipmap.emo_im_pokerface
                R.mipmap.smiley_0,
                R.mipmap.smiley_1,
                R.mipmap.smiley_2,
                R.mipmap.smiley_3,
                R.mipmap.smiley_4,
                R.mipmap.smiley_5,
                R.mipmap.smiley_6,
                R.mipmap.smiley_7,
                R.mipmap.smiley_8,
                R.mipmap.smiley_9,
                R.mipmap.smiley_10,
                R.mipmap.smiley_11,
                R.mipmap.smiley_12,
                R.mipmap.smiley_13,
                R.mipmap.smiley_14,
                R.mipmap.smiley_15,
                R.mipmap.smiley_16,
                R.mipmap.smiley_17,
                R.mipmap.smiley_18,
                R.mipmap.smiley_19,
                R.mipmap.smiley_20,
                R.mipmap.smiley_21,
                R.mipmap.smiley_22,
                R.mipmap.smiley_23,
                R.mipmap.smiley_24,
                R.mipmap.smiley_25,
                R.mipmap.smiley_26,
                R.mipmap.smiley_27,
                R.mipmap.smiley_28,
                R.mipmap.smiley_29,
                R.mipmap.smiley_30,
                R.mipmap.smiley_31,
                R.mipmap.smiley_32,
                R.mipmap.smiley_33,
                R.mipmap.smiley_34,
                R.mipmap.smiley_35,
                R.mipmap.smiley_36,
                R.mipmap.smiley_37,
                R.mipmap.smiley_38,
                R.mipmap.smiley_39,
                R.mipmap.smiley_40,
                R.mipmap.smiley_41,
                R.mipmap.smiley_42,
                R.mipmap.smiley_43,
                R.mipmap.smiley_44,
                R.mipmap.smiley_45,
                R.mipmap.smiley_46,
                R.mipmap.smiley_47,
                R.mipmap.smiley_48,
                R.mipmap.smiley_49,
                R.mipmap.smiley_50,
                R.mipmap.smiley_51,
                R.mipmap.smiley_52,
                R.mipmap.smiley_53,
                R.mipmap.smiley_54,
                R.mipmap.smiley_55,
                R.mipmap.smiley_56,
                R.mipmap.smiley_57,
                R.mipmap.smiley_58,
                R.mipmap.smiley_59,
                R.mipmap.smiley_60,
                R.mipmap.smiley_61,
                R.mipmap.smiley_62,
                R.mipmap.smiley_63,
                R.mipmap.smiley_64,
                R.mipmap.smiley_65,
                R.mipmap.smiley_66,
                R.mipmap.smiley_67,
                R.mipmap.smiley_68,
                R.mipmap.smiley_69,
                R.mipmap.smiley_70,
                R.mipmap.smiley_71,
                R.mipmap.smiley_72,
                R.mipmap.smiley_73,
                R.mipmap.smiley_74,
                R.mipmap.smiley_75,
                R.mipmap.smiley_76,
                R.mipmap.smiley_77,
                R.mipmap.smiley_78,
                R.mipmap.smiley_79,
                R.mipmap.smiley_80,
                R.mipmap.smiley_81,
                R.mipmap.smiley_82,
                R.mipmap.smiley_83,
                R.mipmap.smiley_84,
                R.mipmap.smiley_85,
                R.mipmap.smiley_86,
                R.mipmap.smiley_87,
                R.mipmap.smiley_88,
                R.mipmap.smiley_89,
                R.mipmap.smiley_90,
                R.mipmap.smiley_91,
                R.mipmap.smiley_92,
                R.mipmap.smiley_93,
                R.mipmap.smiley_94,
                R.mipmap.smiley_95,
                R.mipmap.smiley_96,
                R.mipmap.smiley_97,
                R.mipmap.smiley_98,
                R.mipmap.smiley_99,
                R.mipmap.smiley_100,
                R.mipmap.smiley_101,
                R.mipmap.smiley_102,
                R.mipmap.smiley_103,
                R.mipmap.smiley_104,
                R.mipmap.smiley_105,
                R.mipmap.smiley_106,
                R.mipmap.smiley_107,
                R.mipmap.smiley_108,
                R.mipmap.smiley_109,
                R.mipmap.smiley_110,
                R.mipmap.smiley_111,
                R.mipmap.smiley_112,
                R.mipmap.smiley_113,
                R.mipmap.smiley_114,
                R.mipmap.smiley_115,
                R.mipmap.smiley_116,
                R.mipmap.smiley_117,
                R.mipmap.smiley_118,
                R.mipmap.smiley_119

        };

//        public static int HAPPY = 0;
//        public static int SAD = 1;
//        public static int WINKING = 2;
//        public static int TONGUE_STICKING_OUT = 3;
//        public static int SURPRISED = 4;
//        public static int KISSING = 5;
//        public static int YELLING = 6;
//        public static int COOL = 7;
//        public static int MONEY_MOUTH = 8;
//        public static int FOOT_IN_MOUTH = 9;
//        public static int EMBARRASSED = 10;
//        public static int ANGEL = 11;
//        public static int UNDECIDED = 12;
//        public static int CRYING = 13;
//        public static int LIPS_ARE_SEALED = 14;
//        public static int LAUGHING = 15;
//        public static int WTF = 16;
//        public static int MAD = 17;
//        public static int HEART = 18;
//        public static int SMIRK = 19;
//        public static int POKERFACE = 20;

        public static int getSmileyResource(int which) {
            return sIconIds[which];
        }
    }

    // NOTE: if you change anything about this array, you must make the corresponding change
    // to the string arrays: default_smiley_texts and default_smiley_names in res/values/arrays.xml
    public static final int[] DEFAULT_SMILEY_RES_IDS = {
//        Smileys.getSmileyResource(Smileys.HAPPY),                //  0
//        Smileys.getSmileyResource(Smileys.SAD),                  //  1
//        Smileys.getSmileyResource(Smileys.WINKING),              //  2
//        Smileys.getSmileyResource(Smileys.TONGUE_STICKING_OUT),  //  3
//        Smileys.getSmileyResource(Smileys.SURPRISED),            //  4
//        Smileys.getSmileyResource(Smileys.KISSING),              //  5
//        Smileys.getSmileyResource(Smileys.YELLING),              //  6
//        Smileys.getSmileyResource(Smileys.COOL),                 //  7
//        Smileys.getSmileyResource(Smileys.MONEY_MOUTH),          //  8
//        Smileys.getSmileyResource(Smileys.FOOT_IN_MOUTH),        //  9
//        Smileys.getSmileyResource(Smileys.EMBARRASSED),          //  10
//        Smileys.getSmileyResource(Smileys.ANGEL),                //  11
//        Smileys.getSmileyResource(Smileys.UNDECIDED),            //  12
//        Smileys.getSmileyResource(Smileys.CRYING),               //  13
//        Smileys.getSmileyResource(Smileys.LIPS_ARE_SEALED),      //  14
//        Smileys.getSmileyResource(Smileys.LAUGHING),             //  15
//        Smileys.getSmileyResource(Smileys.WTF),                  //  16
//        Smileys.getSmileyResource(Smileys.MAD),                  //  17
//        Smileys.getSmileyResource(Smileys.HEART),                //  18
//        Smileys.getSmileyResource(Smileys.SMIRK),                //  19
//        Smileys.getSmileyResource(Smileys.POKERFACE),            //  20
            Smileys.getSmileyResource(0),
            Smileys.getSmileyResource(1),
            Smileys.getSmileyResource(2),
            Smileys.getSmileyResource(3),
            Smileys.getSmileyResource(4),
            Smileys.getSmileyResource(5),
            Smileys.getSmileyResource(6),
            Smileys.getSmileyResource(7),
            Smileys.getSmileyResource(8),
            Smileys.getSmileyResource(9),
            Smileys.getSmileyResource(10),
            Smileys.getSmileyResource(11),
            Smileys.getSmileyResource(12),
            Smileys.getSmileyResource(13),
            Smileys.getSmileyResource(14),
            Smileys.getSmileyResource(15),
            Smileys.getSmileyResource(16),
            Smileys.getSmileyResource(17),
            Smileys.getSmileyResource(18),
            Smileys.getSmileyResource(19),
            Smileys.getSmileyResource(20),
            Smileys.getSmileyResource(21),
            Smileys.getSmileyResource(22),
            Smileys.getSmileyResource(23),
            Smileys.getSmileyResource(24),
            Smileys.getSmileyResource(25),
            Smileys.getSmileyResource(26),
            Smileys.getSmileyResource(27),
            Smileys.getSmileyResource(28),
            Smileys.getSmileyResource(29),
            Smileys.getSmileyResource(30),
            Smileys.getSmileyResource(31),
            Smileys.getSmileyResource(32),
            Smileys.getSmileyResource(33),
            Smileys.getSmileyResource(34),
            Smileys.getSmileyResource(35),
            Smileys.getSmileyResource(36),
            Smileys.getSmileyResource(37),
            Smileys.getSmileyResource(38),
            Smileys.getSmileyResource(39),
            Smileys.getSmileyResource(40),
            Smileys.getSmileyResource(41),
            Smileys.getSmileyResource(42),
            Smileys.getSmileyResource(43),
            Smileys.getSmileyResource(44),
            Smileys.getSmileyResource(45),
            Smileys.getSmileyResource(46),
            Smileys.getSmileyResource(47),
            Smileys.getSmileyResource(48),
            Smileys.getSmileyResource(49),
            Smileys.getSmileyResource(50),
            Smileys.getSmileyResource(51),
            Smileys.getSmileyResource(52),
            Smileys.getSmileyResource(53),
            Smileys.getSmileyResource(54),
            Smileys.getSmileyResource(55),
            Smileys.getSmileyResource(56),
            Smileys.getSmileyResource(57),
            Smileys.getSmileyResource(58),
            Smileys.getSmileyResource(59),
            Smileys.getSmileyResource(60),
            Smileys.getSmileyResource(61),
            Smileys.getSmileyResource(62),
            Smileys.getSmileyResource(63),
            Smileys.getSmileyResource(64),
            Smileys.getSmileyResource(65),
            Smileys.getSmileyResource(66),
            Smileys.getSmileyResource(67),
            Smileys.getSmileyResource(68),
            Smileys.getSmileyResource(69),
            Smileys.getSmileyResource(70),
            Smileys.getSmileyResource(71),
            Smileys.getSmileyResource(72),
            Smileys.getSmileyResource(73),
            Smileys.getSmileyResource(74),
            Smileys.getSmileyResource(75),
            Smileys.getSmileyResource(76),
            Smileys.getSmileyResource(77),
            Smileys.getSmileyResource(78),
            Smileys.getSmileyResource(79),
            Smileys.getSmileyResource(80),
            Smileys.getSmileyResource(81),
            Smileys.getSmileyResource(82),
            Smileys.getSmileyResource(83),
            Smileys.getSmileyResource(84),
            Smileys.getSmileyResource(85),
            Smileys.getSmileyResource(86),
            Smileys.getSmileyResource(87),
            Smileys.getSmileyResource(88),
            Smileys.getSmileyResource(89),
            Smileys.getSmileyResource(90),
            Smileys.getSmileyResource(91),
            Smileys.getSmileyResource(92),
            Smileys.getSmileyResource(93),
            Smileys.getSmileyResource(94),
            Smileys.getSmileyResource(95),
            Smileys.getSmileyResource(96),
            Smileys.getSmileyResource(97),
            Smileys.getSmileyResource(98),
            Smileys.getSmileyResource(99),
            Smileys.getSmileyResource(100),
            Smileys.getSmileyResource(101),
            Smileys.getSmileyResource(102),
            Smileys.getSmileyResource(103),
            Smileys.getSmileyResource(104),
            Smileys.getSmileyResource(105),
            Smileys.getSmileyResource(106),
            Smileys.getSmileyResource(107),
            Smileys.getSmileyResource(108),
            Smileys.getSmileyResource(109),
            Smileys.getSmileyResource(110),
            Smileys.getSmileyResource(111),
            Smileys.getSmileyResource(112),
            Smileys.getSmileyResource(113),
            Smileys.getSmileyResource(114),
            Smileys.getSmileyResource(115),
            Smileys.getSmileyResource(116),
            Smileys.getSmileyResource(117),
            Smileys.getSmileyResource(118),
            Smileys.getSmileyResource(119),
    };

    public static final int DEFAULT_SMILEY_TEXTS = R.array.smiley_texts;
    public static final int DEFAULT_SMILEY_NAMES = R.array.default_smiley_names;

    /**
     * Builds the hashtable we use for mapping the string version
     * of a smiley (e.g. ":-)") to a resource ID for the icon version.
     */
    private HashMap<String, Integer> buildSmileyToRes() {
        if (DEFAULT_SMILEY_RES_IDS.length != mSmileyTexts.length) {
            // Throw an exception if someone updated DEFAULT_SMILEY_RES_IDS
            // and failed to update arrays.xml
            throw new IllegalStateException("Smiley resource ID/text mismatch");
        }

        HashMap<String, Integer> smileyToRes =
                            new HashMap<String, Integer>(mSmileyTexts.length);
        for (int i = 0; i < mSmileyTexts.length; i++) {
            smileyToRes.put(mSmileyTexts[i], DEFAULT_SMILEY_RES_IDS[i]);
        }

        return smileyToRes;
    }

    /**
     * Builds the regular expression we use to find smileys in {@link #addSmileySpans}.
     */
    private Pattern buildPattern() {
        // Set the StringBuilder capacity with the assumption that the average
        // smiley is 3 characters long.
        StringBuilder patternString = new StringBuilder(mSmileyTexts.length * 3);

        // Build a regex that looks like (:-)|:-(|...), but escaping the smilies
        // properly so they will be interpreted literally by the regex matcher.
        patternString.append('(');
        for (String s : mSmileyTexts) {
            patternString.append(Pattern.quote(s));
            patternString.append('|');
        }
        // Replace the extra '|' with a ')'
        patternString.replace(patternString.length() - 1, patternString.length(), ")");

        return Pattern.compile(patternString.toString());
    }


    /**
     * Adds ImageSpans to a CharSequence that replace textual emoticons such
     * as :-) with a graphical version.
     *
     * @param text A CharSequence possibly containing emoticons
     * @return A CharSequence annotated with ImageSpans covering any
     *         recognized emoticons.
     */
    public CharSequence addSmileySpans(CharSequence text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);

        Matcher matcher = mPattern.matcher(text);
        while (matcher.find()) {
            int resId = mSmileyToRes.get(matcher.group());
            builder.setSpan(new ImageSpan(mContext, resId),
                            matcher.start(), matcher.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return builder;
    }

    public int[] getSmileyResIds(){
        return DEFAULT_SMILEY_RES_IDS;
    }

    public String[] getSmileyTexts(){
        return mSmileyTexts;
    }


}


