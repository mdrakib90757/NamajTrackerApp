package com.example.namajtrackerapp.data

import com.example.namajtrackerapp.model.DailyPrayerRecord
import com.example.namajtrackerapp.model.EventCategory
import com.example.namajtrackerapp.model.IslamicEvent
import com.example.namajtrackerapp.model.PrayerStatus
import com.example.namajtrackerapp.model.PrayerType
import com.example.namajtrackerapp.model.SpiritualNote
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object SampleDataProvider {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    fun getTodayDateStr(): String = dateFormat.format(Date())

    fun getDateStrDaysAgo(daysAgo: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return dateFormat.format(cal.time)
    }

    fun getDateStrDaysAhead(daysAhead: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, daysAhead)
        return dateFormat.format(cal.time)
    }

    fun getInitialPrayerRecords(): Map<String, DailyPrayerRecord> {
        val records = mutableMapOf<String, DailyPrayerRecord>()
        
        // Today - 4 done so far, Isha pending
        val todayStr = getTodayDateStr()
        records[todayStr] = DailyPrayerRecord(
            dateStr = todayStr,
            prayers = mapOf(
                PrayerType.FAJR to PrayerStatus.ON_TIME,
                PrayerType.DHUHR to PrayerStatus.ON_TIME,
                PrayerType.ASR to PrayerStatus.ON_TIME,
                PrayerType.MAGHRIB to PrayerStatus.ON_TIME,
                PrayerType.ISHA to PrayerStatus.NOT_YET,
                PrayerType.TAHAJJUD to PrayerStatus.ON_TIME,
                PrayerType.DUHA to PrayerStatus.ON_TIME,
                PrayerType.WITR to PrayerStatus.NOT_YET
            ),
            prayerNotes = mapOf(
                PrayerType.FAJR to "Felt deep tranquility during Surah Ar-Rahman recitation."
            )
        )

        // Past 28 days with rich realistic data
        for (day in 1..28) {
            val dateStr = getDateStrDaysAgo(day)
            val isFullOnTime = day % 4 != 0
            val isLate = day % 4 == 0 && day % 8 != 0
            
            val fajrStatus = if (isFullOnTime) PrayerStatus.ON_TIME else if (isLate) PrayerStatus.LATE else PrayerStatus.ON_TIME
            val dhuhrStatus = if (isLate) PrayerStatus.LATE else PrayerStatus.ON_TIME
            val asrStatus = PrayerStatus.ON_TIME
            val maghribStatus = PrayerStatus.ON_TIME
            val ishaStatus = if (day % 14 == 0) PrayerStatus.MISSED else PrayerStatus.ON_TIME

            records[dateStr] = DailyPrayerRecord(
                dateStr = dateStr,
                prayers = mapOf(
                    PrayerType.FAJR to fajrStatus,
                    PrayerType.DHUHR to dhuhrStatus,
                    PrayerType.ASR to asrStatus,
                    PrayerType.MAGHRIB to maghribStatus,
                    PrayerType.ISHA to ishaStatus,
                    PrayerType.WITR to PrayerStatus.ON_TIME
                )
            )
        }

        return records
    }

    fun getInitialEvents(): List<IslamicEvent> {
        val cal = Calendar.getInstance()
        val currentYear = cal.get(Calendar.YEAR)

        return listOf(
            IslamicEvent(
                id = "evt_hijri_new_year",
                titleEn = "Islamic New Year 1448",
                titleBn = "পবিত্র হিজরি নববর্ষ (১ মহররম)",
                dateStr = "$currentYear-01-10",
                hijriDateEn = "1 Muharram 1448 AH",
                hijriDateBn = "১ মহররম ১৪৪৮ হিজরি",
                noteEn = "Beginning of the new Islamic lunar year. A time for reflection and renewal of intentions.",
                noteBn = "নতুন হিজরি বছরের সূচনা। তাওবা ও নতুন সংকল্প গ্রহণের বিশেষ দিন।",
                isRecurringYearly = true,
                category = EventCategory.SPECIAL_NIGHT
            ),
            IslamicEvent(
                id = "evt_shab_e_barat",
                titleEn = "Shab-e-Barat (15th Shaban)",
                titleBn = "পবিত্র শবে বরাত (১৫ শাবান)",
                dateStr = "$currentYear-02-14",
                hijriDateEn = "15 Shaban 1446 AH",
                hijriDateBn = "১৫ শাবান ১৪৪৬ হিজরি",
                noteEn = "Night of forgiveness, seeking repentance and making dua for the upcoming Ramadan.",
                noteBn = "মহিমান্বিত ক্ষমা ও ক্ষমার রাত, অধিক পরিমাণে ইস্তিগফার ও রোজা রাখার নিয়ত।",
                isRecurringYearly = true,
                category = EventCategory.SPECIAL_NIGHT
            ),
            IslamicEvent(
                id = "evt_ramadan_start",
                titleEn = "First Day of Ramadan 1446",
                titleBn = "পবিত্র রমজানুল মোবারক শুরু",
                dateStr = "$currentYear-03-01",
                hijriDateEn = "1 Ramadan 1446 AH",
                hijriDateBn = "১ রমজান ১৪৪৬ হিজরি",
                noteEn = "Beginning of blessed fasting month. Target: Complete recitation of entire Quran.",
                noteBn = "বরকতময় সিয়াম সাধনার মাস শুরু। লক্ষ্য: পূর্ণ কুরআন তিলাওয়াত ও তারাবীহ নামাজ।",
                isRecurringYearly = true,
                category = EventCategory.RAMADAN
            ),
            IslamicEvent(
                id = "evt_laylatul_qadr",
                titleEn = "Laylat al-Qadr (Night of Power)",
                titleBn = "পবিত্র শবে কদর (লায়লাতুল কদর)",
                dateStr = "$currentYear-03-26",
                hijriDateEn = "27 Ramadan 1446 AH",
                hijriDateBn = "২৭ রমজান ১৪৪৬ হিজরি",
                noteEn = "Better than a thousand months. Stand in night prayers with heartfelt supplication.",
                noteBn = "হাজার মাসের চেয়ে উত্তম রাত। তাহাজ্জুদ ও রবের দরবারে আন্তরিক কান্নাকাটি।",
                isRecurringYearly = true,
                category = EventCategory.SPECIAL_NIGHT
            ),
            IslamicEvent(
                id = "evt_eid_ul_fitr",
                titleEn = "Eid al-Fitr Celebrations",
                titleBn = "পবিত্র ঈদুল ফিতর",
                dateStr = "$currentYear-03-31",
                hijriDateEn = "1 Shawwal 1446 AH",
                hijriDateBn = "১ শাওয়াল ১৪৪৬ হিজরি",
                noteEn = "Give Zakat al-Fitr before Eid prayer, wear clean clothes and spread joy.",
                noteBn = "ঈদের নামাজের পূর্বে ফিতরা প্রদান এবং আত্মীয়-স্বজনের সাথে আনন্দ ভাগাভাগি।",
                isRecurringYearly = true,
                category = EventCategory.EID
            ),
            IslamicEvent(
                id = "evt_shawwal_fasting",
                titleEn = "Six Days of Shawwal Fasting",
                titleBn = "শাওয়াল মাসের ৬ রোজা (সুন্নাত)",
                dateStr = "$currentYear-04-10",
                hijriDateEn = "10 Shawwal 1446 AH",
                hijriDateBn = "১০ শাওয়াল ১৪৪৬ হিজরি",
                noteEn = "Fasting 6 days in Shawwal after Ramadan equals fasting the whole year.",
                noteBn = "রমজানের পর শাওয়ালের ৬ টি রোজা সারা বছর রোজা রাখার সওয়াব এনে দেয়।",
                isRecurringYearly = true,
                category = EventCategory.SUNNAH_FAST
            ),
            IslamicEvent(
                id = "evt_may_ayyam_beed",
                titleEn = "Ayyam al-Beed (White Days Fasting)",
                titleBn = "মে মাসের আইয়ামে বিজ এর রোজা",
                dateStr = "$currentYear-05-15",
                hijriDateEn = "14 Dhul Qadah 1446 AH",
                hijriDateBn = "১৪ জিলকদ ১৪৪৬ হিজরি",
                noteEn = "Sunnah fasting on full moon days of lunar month.",
                noteBn = "প্রতি চন্দ্রমাসের ১৩, ১৪ ও ১৫ তারিখের সুন্নাত সিয়াম।",
                isRecurringYearly = false,
                category = EventCategory.SUNNAH_FAST
            ),
            IslamicEvent(
                id = "evt_day_of_arafah",
                titleEn = "Day of Arafah",
                titleBn = "পবিত্র ইয়াওমে আরাফাহ",
                dateStr = "$currentYear-06-05",
                hijriDateEn = "9 Dhul Hijjah 1446 AH",
                hijriDateBn = "৯ জিলহজ্জ ১৪৪৬ হিজরি",
                noteEn = "Fasting on Arafah expiates sins of previous and upcoming year.",
                noteBn = "আরাফার দিনের রোজা পূর্ববর্তী ও পরবর্তী বছরের গুনাহ মাফের কারণ।",
                isRecurringYearly = true,
                category = EventCategory.SUNNAH_FAST
            ),
            IslamicEvent(
                id = "evt_eid_ul_adha",
                titleEn = "Eid al-Adha (Feast of Sacrifice)",
                titleBn = "পবিত্র ঈদুল আযহা (কোরবানি ঈদ)",
                dateStr = "$currentYear-06-06",
                hijriDateEn = "10 Dhul Hijjah 1446 AH",
                hijriDateBn = "১০ জিলহজ্জ ১৪৪৬ হিজরি",
                noteEn = "Sacrifice in obedience to Allah, distribute meat to the poor and needy.",
                noteBn = "আল্লাহর সন্তুষ্টির জন্য কুরবানী এবং দরিদ্র ও আত্মীয়দের মাঝে গোশত বণ্টন।",
                isRecurringYearly = true,
                category = EventCategory.EID
            ),
            IslamicEvent(
                id = "evt_ashura_fasting",
                titleEn = "Day of Ashura (10th Muharram)",
                titleBn = "পবিত্র আশুরার রোজা (১০ মহররম)",
                dateStr = "$currentYear-07-16",
                hijriDateEn = "10 Muharram 1447 AH",
                hijriDateBn = "১০ মহররম ১৪৪৭ হিজরি",
                noteEn = "Fasting on 9th and 10th Muharram forgives past year's minor sins.",
                noteBn = "আশুরার রোজা পূর্ববর্তী এক বছরের গুনাহ মাফের উসীলা।",
                isRecurringYearly = true,
                category = EventCategory.SUNNAH_FAST
            ),
            IslamicEvent(
                id = "evt_august_sunnah_fast",
                titleEn = "Monday & Thursday Sunnah Fast",
                titleBn = "সোম ও বৃহস্পতিবারের সুন্নাত রোজা",
                dateStr = "$currentYear-08-10",
                hijriDateEn = "26 Safar 1447 AH",
                hijriDateBn = "২৬ সফর ১৪৪৭ হিজরি",
                noteEn = "Deeds are presented to Allah on Mondays and Thursdays.",
                noteBn = "সোম ও বৃহস্পতিবার বান্দার আমল আল্লাহর দরবারে পেশ করা হয়।",
                isRecurringYearly = false,
                category = EventCategory.SUNNAH_FAST
            ),
            IslamicEvent(
                id = "evt_august_quran_goal",
                titleEn = "Monthly Quran Recitation Goal",
                titleBn = "মাসিক কুরআন তিলাওয়াত ও তাফসীর",
                dateStr = "$currentYear-08-25",
                hijriDateEn = "12 Rabi al-Awwal 1447 AH",
                hijriDateBn = "১২ রবিউল আউয়াল ১৪৪৭ হিজরি",
                noteEn = "Complete 5 Juz recitation with Bengali translation and reflection.",
                noteBn = "অর্থসহ ৫ পারা কুরআন কারীম অধ্যয়ন ও চিন্তা-ভাবনা।",
                isRecurringYearly = false,
                category = EventCategory.PERSONAL
            ),
            IslamicEvent(
                id = "evt_seerah_study",
                titleEn = "Seerah Study Circle",
                titleBn = "সীরাতুন্নবী (সা.) অধ্যয়ন মজলিস",
                dateStr = "$currentYear-09-12",
                hijriDateEn = "12 Rabi al-Awwal 1447 AH",
                hijriDateBn = "১২ রবিউল আউয়াল ১৪৪৭ হিজরি",
                noteEn = "Study the life, character and teachings of Prophet Muhammad (PBUH).",
                noteBn = "রাসূলুল্লাহ (সা.)-এর সুন্নাত ও জীবনচরিত অনুধরনের বিশেষ আমল।",
                isRecurringYearly = true,
                category = EventCategory.SPECIAL_NIGHT
            ),
            IslamicEvent(
                id = "evt_oct_ayyam_beed",
                titleEn = "October White Days Fasting",
                titleBn = "অক্টোবর মাসের আইয়ামে বিজ সিয়াম",
                dateStr = "$currentYear-10-24",
                hijriDateEn = "13 Rabi al-Thani 1447 AH",
                hijriDateBn = "১৩ রবিউস সানি ১৪৪৭ হিজরি",
                noteEn = "Sunnah fasting on 13, 14, 15 of lunar month.",
                noteBn = "আইয়ামে বিজের তিন দিন রোজা পালন।",
                isRecurringYearly = false,
                category = EventCategory.SUNNAH_FAST
            ),
            IslamicEvent(
                id = "evt_nov_charity",
                titleEn = "Winter Clothing Charity Drive",
                titleBn = "শীতবস্ত্র বিতরণ ও দান-সদকাহ",
                dateStr = "$currentYear-11-15",
                hijriDateEn = "5 Jumada al-Awwal 1447 AH",
                hijriDateBn = "৫ জমাদিউল আউয়াল ১৪৪৭ হিজরি",
                noteEn = "Distribute winter clothes and food packages to needy believers.",
                noteBn = "অসহায় মানুষদের মাঝে শীতবস্ত্র ও প্রয়োজনীয় সহায়তা প্রদান।",
                isRecurringYearly = false,
                category = EventCategory.PERSONAL
            ),
            IslamicEvent(
                id = "evt_rajab_prep",
                titleEn = "Preparation for Sacred Month Rajab",
                titleBn = "পবিত্র রজব মাসের আমলের প্রস্তুতি",
                dateStr = "$currentYear-12-20",
                hijriDateEn = "1 Rajab 1447 AH",
                hijriDateBn = "১ রজব ১৪৪৭ হিজরি",
                noteEn = "Dua: O Allah bless us in Rajab and Shaban and allow us to reach Ramadan.",
                noteBn = "দোয়া: হে আল্লাহ! আমাদের রজব ও শাবান মাসে বরকত দিন এবং রমজান পর্যন্ত পৌঁছে দিন।",
                isRecurringYearly = true,
                category = EventCategory.SPECIAL_NIGHT
            )
        )
    }

    fun getInitialNotes(): List<SpiritualNote> {
        val todayStr = getTodayDateStr()
        val yesterdayStr = getDateStrDaysAgo(1)
        val threeDaysAgoStr = getDateStrDaysAgo(3)
        val fiveDaysAgoStr = getDateStrDaysAgo(5)

        return listOf(
            SpiritualNote(
                id = "note_1",
                title = "Fajr Serenity & Surah Ad-Duha",
                content = "Reflected on the verse: 'And your Lord is going to give you, and you will be satisfied' (93:5). A powerful reminder that hardships in this Dunya are temporary, and Allah's mercy is boundless.",
                dateStr = todayStr,
                linkedPrayer = PrayerType.FAJR,
                tags = listOf("Tadabbur", "Fajr", "Ayah Reflection")
            ),
            SpiritualNote(
                id = "note_2",
                title = "Dua for Steadfastness in Salah",
                content = "رَبِّ اجْعَلْنِي مُقِيمَ الصَّلَاةِ وَمِن ذُرِّيَّتِي ۚ رَبَّنَا وَتَقَبَّلْ دُعَاءِ\n'My Lord, make me an establisher of prayer, and from my descendants. Our Lord, and accept my supplication.' (Surah Ibrahim 14:40). Recited after Dhuhr.",
                dateStr = yesterdayStr,
                linkedPrayer = PrayerType.DHUHR,
                tags = listOf("Dua", "Quran", "Consistency")
            ),
            SpiritualNote(
                id = "note_3",
                title = "Tahajjud & Istighfar Experience",
                content = "Woke up 30 minutes before Fajr. The absolute stillness of the night allows words of dua to flow directly from the heart without worldly distractions. Target: Keep this 3 nights a week.",
                dateStr = threeDaysAgoStr,
                linkedPrayer = PrayerType.TAHAJJUD,
                tags = listOf("Tahajjud", "Nawafil", "Habit")
            ),
            SpiritualNote(
                id = "note_4",
                title = "Preparation for the Holy Month of Ramadan",
                content = "Goals this year:\n1. Recite 1 Juz daily with understanding\n2. Pray all 20 Taraweeh in congregation\n3. Give daily micro-charity (Sadaqah)\n4. Guard the tongue and practice mindful presence in all 5 prayers.",
                dateStr = fiveDaysAgoStr,
                linkedPrayer = null,
                tags = listOf("Ramadan", "Goals", "Spiritual Plan")
            )
        )
    }
}
