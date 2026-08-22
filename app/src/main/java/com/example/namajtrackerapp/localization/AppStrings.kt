package com.example.namajtrackerapp.localization

import com.example.namajtrackerapp.model.AppLanguage
import com.example.namajtrackerapp.model.EventCategory
import com.example.namajtrackerapp.model.PrayerStatus
import com.example.namajtrackerapp.model.PrayerType

object AppStrings {

    fun appTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Namaz Tracker"
        AppLanguage.BANGLA -> "নামাজ ট্র্যাকার"
    }

    fun appSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Mindful Islamic Prayer & Habit Tracker"
        AppLanguage.BANGLA -> "আপনার প্রাত্যহিক নামাজ ও আমল ট্র্যাকার"
    }

    // Tabs
    fun tabHome(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Today"
        AppLanguage.BANGLA -> "আজকের নামাজ"
    }

    fun tabCalendar(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Calendar"
        AppLanguage.BANGLA -> "ক্যালেন্ডার"
    }

    fun tabEvents(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Events"
        AppLanguage.BANGLA -> "দিনপঞ্জিকা"
    }

    fun tabNotes(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Reflections"
        AppLanguage.BANGLA -> "নোটস ও দোয়া"
    }

    fun tabProfile(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Profile"
        AppLanguage.BANGLA -> "প্রোফাইল"
    }

    // Prayer Names
    fun prayerName(prayer: PrayerType, lang: AppLanguage): String = when (prayer) {
        PrayerType.FAJR -> when (lang) {
            AppLanguage.ENGLISH -> "Fajr"
            AppLanguage.BANGLA -> "ফজর"
        }
        PrayerType.DHUHR -> when (lang) {
            AppLanguage.ENGLISH -> "Dhuhr"
            AppLanguage.BANGLA -> "যোহর"
        }
        PrayerType.ASR -> when (lang) {
            AppLanguage.ENGLISH -> "Asr"
            AppLanguage.BANGLA -> "আসর"
        }
        PrayerType.MAGHRIB -> when (lang) {
            AppLanguage.ENGLISH -> "Maghrib"
            AppLanguage.BANGLA -> "মাগরিব"
        }
        PrayerType.ISHA -> when (lang) {
            AppLanguage.ENGLISH -> "Isha"
            AppLanguage.BANGLA -> "এশা"
        }
        PrayerType.TAHAJJUD -> when (lang) {
            AppLanguage.ENGLISH -> "Tahajjud"
            AppLanguage.BANGLA -> "তাহাজ্জুদ"
        }
        PrayerType.DUHA -> when (lang) {
            AppLanguage.ENGLISH -> "Duha / Chasht"
            AppLanguage.BANGLA -> "দুহা / চাশত"
        }
        PrayerType.WITR -> when (lang) {
            AppLanguage.ENGLISH -> "Witr"
            AppLanguage.BANGLA -> "বিতর"
        }
    }

    fun prayerDescription(prayer: PrayerType, lang: AppLanguage): String = when (prayer) {
        PrayerType.FAJR -> when (lang) {
            AppLanguage.ENGLISH -> "Dawn Prayer (2 Fard + 2 Sunnah)"
            AppLanguage.BANGLA -> "ভোরের নামাজ (২ রাকাত ফরজ + ২ রাকাত সুন্নত)"
        }
        PrayerType.DHUHR -> when (lang) {
            AppLanguage.ENGLISH -> "Noon Prayer (4 Fard + 4 Sunnah)"
            AppLanguage.BANGLA -> "দুপুরের নামাজ (৪ রাকাত ফরজ + সুন্নত)"
        }
        PrayerType.ASR -> when (lang) {
            AppLanguage.ENGLISH -> "Afternoon Prayer (4 Fard)"
            AppLanguage.BANGLA -> "বিকেলের নামাজ (৪ রাকাত ফরজ)"
        }
        PrayerType.MAGHRIB -> when (lang) {
            AppLanguage.ENGLISH -> "Sunset Prayer (3 Fard + 2 Sunnah)"
            AppLanguage.BANGLA -> "সন্ধ্যার নামাজ (৩ রাকাত ফরজ + ২ রাকাত সুন্নত)"
        }
        PrayerType.ISHA -> when (lang) {
            AppLanguage.ENGLISH -> "Night Prayer (4 Fard + 2 Sunnah)"
            AppLanguage.BANGLA -> "রাতের নামাজ (৪ রাকাত ফরজ + ২ রাকাত সুন্নত)"
        }
        PrayerType.TAHAJJUD -> when (lang) {
            AppLanguage.ENGLISH -> "Late Night Vigil Prayer (Nawafil)"
            AppLanguage.BANGLA -> "রাতের শেষ প্রহরে নফল নামাজ"
        }
        PrayerType.DUHA -> when (lang) {
            AppLanguage.ENGLISH -> "Mid-morning Sunnah Prayer"
            AppLanguage.BANGLA -> "পূর্বাহ্নের সুন্নত নামাজ"
        }
        PrayerType.WITR -> when (lang) {
            AppLanguage.ENGLISH -> "Night Concluding Prayer (3 Wajib)"
            AppLanguage.BANGLA -> "রাতের সমাপ্তি নামাজ (৩ রাকাত ওয়াজিব)"
        }
    }

    fun prayerRakatSummary(prayer: PrayerType, lang: AppLanguage): String = when (prayer) {
        PrayerType.FAJR -> if (lang == AppLanguage.ENGLISH) "2 Fard + 2 Sunnah" else "২ ফরজ + ২ সুন্নত"
        PrayerType.DHUHR -> if (lang == AppLanguage.ENGLISH) "4 Fard + 4 Sunnah" else "৪ ফরজ + ৪ সুন্নত"
        PrayerType.ASR -> if (lang == AppLanguage.ENGLISH) "4 Fard" else "৪ ফরজ"
        PrayerType.MAGHRIB -> if (lang == AppLanguage.ENGLISH) "3 Fard + 2 Sunnah" else "৩ ফরজ + ২ সুন্নত"
        PrayerType.ISHA -> if (lang == AppLanguage.ENGLISH) "4 Fard + 2 Sunnah" else "৪ ফরজ + ২ সুন্নত"
        PrayerType.TAHAJJUD -> if (lang == AppLanguage.ENGLISH) "Nawafil" else "নফল"
        PrayerType.DUHA -> if (lang == AppLanguage.ENGLISH) "Sunnah" else "সুন্নত"
        PrayerType.WITR -> if (lang == AppLanguage.ENGLISH) "3 Wajib" else "৩ ওয়াজিব"
    }

    // Status Names
    fun statusName(status: PrayerStatus, lang: AppLanguage): String = when (status) {
        PrayerStatus.ON_TIME -> when (lang) {
            AppLanguage.ENGLISH -> "On Time"
            AppLanguage.BANGLA -> "যথাসময়ে"
        }
        PrayerStatus.LATE -> when (lang) {
            AppLanguage.ENGLISH -> "Late"
            AppLanguage.BANGLA -> "দেরিতে"
        }
        PrayerStatus.MISSED -> when (lang) {
            AppLanguage.ENGLISH -> "Missed (Qaza)"
            AppLanguage.BANGLA -> "কাযা"
        }
        PrayerStatus.NOT_YET -> when (lang) {
            AppLanguage.ENGLISH -> "Not Yet"
            AppLanguage.BANGLA -> "এখনো হয়নি"
        }
    }

    // Home / Today Screen
    fun todayProgressTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Today's Sacred Prayers"
        AppLanguage.BANGLA -> "আজকের ৫ ওয়াক্ত নামাজ"
    }

    fun completedOfFive(count: Int, lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "$count of 5 completed"
        AppLanguage.BANGLA -> "৫টির মধ্যে $count টি আদায় সম্পন্ন"
    }

    fun streakDays(count: Int, lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "$count Day Streak"
        AppLanguage.BANGLA -> "$count দিনের ধারাবাহিকতা"
    }

    fun streakSubtext(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Consistency is beloved to Allah"
        AppLanguage.BANGLA -> "নিয়মিত আমল আল্লাহর কাছে সবচেয়ে প্রিয়"
    }

    fun markAllOnTime(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Quick Log: Mark Done"
        AppLanguage.BANGLA -> "দ্রুত সব আদায় চিহ্নিত করুন"
    }

    fun sunnahSectionTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Sunnah & Nawafil Prayers"
        AppLanguage.BANGLA -> "সুন্নত ও নফল নামাজ"
    }

    fun dailyReflectionTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Daily Ayah & Reflection"
        AppLanguage.BANGLA -> "আজকের আয়াত ও অনুপ্রেরণা"
    }

    fun addNoteForPrayer(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Add note for this prayer"
        AppLanguage.BANGLA -> "এই নামাজের জন্য নোট যোগ করুন"
    }

    fun tapToChangeStatus(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Tap to change status"
        AppLanguage.BANGLA -> "স্ট্যাটাস পরিবর্তন করতে ট্যাপ করুন"
    }

    // Calendar Screen
    fun calendarTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Prayer History & Insights"
        AppLanguage.BANGLA -> "নামাজের ইতিহাস ও পরিসংখ্যান"
    }

    fun monthlyRate(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Monthly Rate"
        AppLanguage.BANGLA -> "মাসিক আদায়ের হার"
    }

    fun weeklyRate(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Weekly Rate"
        AppLanguage.BANGLA -> "সাপ্তাহিক হার"
    }

    fun statsBreakdown(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Status Breakdown"
        AppLanguage.BANGLA -> "অবস্থার বিবরণ"
    }

    fun toBanglaDigits(str: String): String {
        val bnDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
        return str.map { if (it.isDigit()) bnDigits[it - '0'] else it }.joinToString("")
    }

    fun formatHeaderDate(date: java.util.Date = java.util.Date(), lang: AppLanguage): String {
        val cal = java.util.Calendar.getInstance().apply { time = date }
        val dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK)
        val dayOfMonth = cal.get(java.util.Calendar.DAY_OF_MONTH)
        val month = cal.get(java.util.Calendar.MONTH)
        val year = cal.get(java.util.Calendar.YEAR)

        if (lang == AppLanguage.ENGLISH) {
            val sdf = java.text.SimpleDateFormat("EEEE, d MMMM yyyy", java.util.Locale.US)
            return sdf.format(date)
        }

        val daysBn = arrayOf("", "রবিবার", "সোমবার", "মঙ্গলবার", "বুধবার", "বৃহস্পতিবার", "শুক্রবার", "শনিবার")
        val monthsBn = arrayOf("জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন", "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর")

        val dayName = daysBn.getOrElse(dayOfWeek) { "" }
        val monthName = monthsBn.getOrElse(month) { "" }
        val dayNumBn = toBanglaDigits(dayOfMonth.toString())
        val yearNumBn = toBanglaDigits(year.toString())

        return "$dayName, $dayNumBn $monthName $yearNumBn"
    }

    fun selectedDayTitle(date: String, lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Prayers for $date"
        AppLanguage.BANGLA -> "${toBanglaDigits(date)} তারিখের নামাজের বিবরণ"
    }

    fun tapDayToEdit(language: AppLanguage): String {
        return if (language == AppLanguage.ENGLISH) {
            "Tap any prayer to update its status for\nthis date"
        } else {
            "এই তারিখের স্ট্যাটাস হালনাগাদ করতে যেকোনো নামাজে\nট্যাপ করুন"
        }
    }

    fun legendOnTime(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "All On Time"
        AppLanguage.BANGLA -> "সব সময়মতো"
    }

    fun legendPartial(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Partial / Late"
        AppLanguage.BANGLA -> "আংশিক / দেরিতে"
    }

    fun legendMissed(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Missed / Qaza"
        AppLanguage.BANGLA -> "কাযা"
    }

    // Events Screen
    fun eventsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Islamic Events & Days"
        AppLanguage.BANGLA -> "ইসলামিক ও বিশেষ দিনপঞ্জিকা"
    }

    fun totalEvents(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Total Events"
        AppLanguage.BANGLA -> "মোট বিশেষ দিন"
    }

    fun thisYear(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "This Year"
        AppLanguage.BANGLA -> "এই বছরে"
    }

    fun lifetime(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Lifetime"
        AppLanguage.BANGLA -> "সর্বমোট"
    }

    fun addEvent(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Add Event"
        AppLanguage.BANGLA -> "নতুন দিবস যোগ করুন"
    }

    fun editEvent(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Edit Event"
        AppLanguage.BANGLA -> "দিবস সম্পাদনা"
    }

    fun eventTitleLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Event Title"
        AppLanguage.BANGLA -> "দিবসের নাম"
    }

    fun eventDateLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Date"
        AppLanguage.BANGLA -> "তারিখ"
    }

    fun recurringYearly(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Recurring Yearly"
        AppLanguage.BANGLA -> "প্রতি বছর পুনরাবৃত্তি"
    }

    fun categoryLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Category"
        AppLanguage.BANGLA -> "শ্রেণী"
    }

    fun eventNoteLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Personal Note / Intention"
        AppLanguage.BANGLA -> "ব্যক্তিগত নোট বা নিয়ত"
    }

    fun categoryName(category: EventCategory, lang: AppLanguage) = when (category) {
        EventCategory.RAMADAN -> when (lang) {
            AppLanguage.ENGLISH -> "Ramadan"
            AppLanguage.BANGLA -> "রমজান"
        }
        EventCategory.EID -> when (lang) {
            AppLanguage.ENGLISH -> "Eid"
            AppLanguage.BANGLA -> "ঈদ"
        }
        EventCategory.SUNNAH_FAST -> when (lang) {
            AppLanguage.ENGLISH -> "Sunnah Fasting"
            AppLanguage.BANGLA -> "নফল রোজা"
        }
        EventCategory.SPECIAL_NIGHT -> when (lang) {
            AppLanguage.ENGLISH -> "Blessed Night"
            AppLanguage.BANGLA -> "মহিমান্বিত রাত"
        }
        EventCategory.PERSONAL -> when (lang) {
            AppLanguage.ENGLISH -> "Personal / Family"
            AppLanguage.BANGLA -> "ব্যক্তিগত / পারিবারিক"
        }
    }

    fun noEventsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "No Islamic Events Yet"
        AppLanguage.BANGLA -> "এখনো কোনো বিশেষ দিবস নেই"
    }

    fun noEventsDesc(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Track upcoming blessed nights, Ramadan, Eid dates, and personal fasting intentions."
        AppLanguage.BANGLA -> "মহিমান্বিত রাত, রমজান, ঈদের তারিখ ও ব্যক্তিগত রোজার নিয়ত সংরক্ষণ করতে প্লাস বাটনে ট্যাপ করুন।"
    }

    // Notes Screen
    fun notesTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Spiritual Notes & Duas"
        AppLanguage.BANGLA -> "আধ্যাত্মিক নোট ও দোয়া"
    }

    fun addNote(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Add Note"
        AppLanguage.BANGLA -> "নতুন নোট লিখুন"
    }

    fun editNote(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Edit Note"
        AppLanguage.BANGLA -> "নোট সম্পাদনা"
    }

    fun searchNotes(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Search notes, duas, reflections..."
        AppLanguage.BANGLA -> "নোট, দোয়া বা আয়াত অনুসন্ধান করুন..."
    }

    fun noteTitleLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Title"
        AppLanguage.BANGLA -> "শিরোনাম"
    }

    fun noteContentLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Your reflection, dua or Ayah..."
        AppLanguage.BANGLA -> "আপনার চিন্তা, প্রিয় দোয়া বা আয়াত লিখুন..."
    }

    fun linkToPrayerOptional(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Link to a Prayer (Optional)"
        AppLanguage.BANGLA -> "নামাজের সাথে যুক্ত করুন (ঐচ্ছিক)"
    }

    fun none(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "None"
        AppLanguage.BANGLA -> "কোনোটি নয়"
    }

    fun tagsLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Tag"
        AppLanguage.BANGLA -> "ট্যাগ"
    }

    fun noNotesTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Your Journal is Empty"
        AppLanguage.BANGLA -> "আপনার ডায়েরি এখনো খালি"
    }

    fun noNotesDesc(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Write down reflections after Salah, Ayahs that touched your heart, and heartfelt Duas."
        AppLanguage.BANGLA -> "নামাজের পরের আত্মশুদ্ধির ভাবনা, প্রিয় আয়াত ও মনের দোয়াগুলো সুন্দরভাবে লিখে রাখুন।"
    }

    // Delete Confirmation Dialog
    fun deleteConfirmTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Delete Confirmation"
        AppLanguage.BANGLA -> "মুছে ফেলার নিশ্চিতকরণ"
    }

    fun deleteConfirmMessage(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Are you sure you want to delete this? This can't be undone."
        AppLanguage.BANGLA -> "আপনি কি নিশ্চিতভাবে এটি মুছে ফেলতে চান? এটি আর ফিরিয়ে আনা সম্ভব হবে না।"
    }

    fun cancel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Cancel"
        AppLanguage.BANGLA -> "বাতিল"
    }

    fun delete(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Delete"
        AppLanguage.BANGLA -> "মুছে ফেলুন"
    }

    fun save(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Save"
        AppLanguage.BANGLA -> "সংরক্ষণ করুন"
    }

    // Profile / Settings Screen
    fun profileTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Profile & Preferences"
        AppLanguage.BANGLA -> "প্রোফাইল ও সেটিংস"
    }

    fun userNameLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Your Name / Title"
        AppLanguage.BANGLA -> "আপনার নাম / উপাধি"
    }

    fun selectAvatar(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Choose Avatar"
        AppLanguage.BANGLA -> "অবতার নির্বাচন করুন"
    }

    fun languageLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Language / ভাষা"
        AppLanguage.BANGLA -> "ভাষা / Language"
    }

    fun themeModeLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Theme Mode"
        AppLanguage.BANGLA -> "অ্যাপের থিম"
    }

    fun themeSystem(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "System"
        AppLanguage.BANGLA -> "সিস্টেম"
    }

    fun themeLight(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Light"
        AppLanguage.BANGLA -> "লাইট"
    }

    fun themeDark(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Dark"
        AppLanguage.BANGLA -> "ডার্ক"
    }

    fun lifetimeStats(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Lifetime Journey Summary"
        AppLanguage.BANGLA -> "সর্বমোট আমলের সারসংক্ষেপ"
    }

    fun totalPrayersLogged(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Total Prayers Logged"
        AppLanguage.BANGLA -> "মোট নথিভুক্ত নামাজ"
    }

    fun onTimePercentage(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "On-Time Ratio"
        AppLanguage.BANGLA -> "যথাসময়ে আদায়ের অনুপাত"
    }

    fun totalNotesCount(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Spiritual Notes"
        AppLanguage.BANGLA -> "সংরক্ষিত নোট"
    }

    fun totalEventsCount(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Islamic Events"
        AppLanguage.BANGLA -> "সংরক্ষিত দিবস"
    }

    fun replayOnboarding(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Replay Welcome Tour"
        AppLanguage.BANGLA -> "ওয়েলকাম ট্যুর পুনরায় দেখুন"
    }

    fun resetDemoData(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Reload Sample Data"
        AppLanguage.BANGLA -> "নমুনা ডাটা পুনরায় লোড করুন"
    }

    fun privacyTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "100% Offline & Private"
        AppLanguage.BANGLA -> "১০০% অফলাইন ও ব্যক্তিগত"
    }

    fun privacyDesc(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "All your prayer records, notes, and events remain solely on this device. No logins, no cloud tracking, and complete peace of mind."
        AppLanguage.BANGLA -> "আপনার সমস্ত নামাজের তথ্য, নোট ও দিবস সম্পূর্ণভাবে কেবল আপনার ফোনেই সংরক্ষিত থাকে। কোনো ইন্টারনেট বা ক্লাউড ট্র্যাকিং নেই।"
    }

    // Onboarding
    fun onboardingSkip(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Skip"
        AppLanguage.BANGLA -> "এড়িয়ে যান"
    }

    fun onboardingNext(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Next"
        AppLanguage.BANGLA -> "পরবর্তী"
    }

    fun onboardingGetStarted(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Begin Your Journey"
        AppLanguage.BANGLA -> "শুরু করুন"
    }

    fun onboardingStep1Title(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Establish Your Sacred Rhythm"
        AppLanguage.BANGLA -> "দৈনিক ৫ ওয়াক্ত নামাজের ধারাবাহিকতা"
    }

    fun onboardingStep1Desc(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Build a mindful, consistent Salah habit with an elegant, warm, and uncluttered tracker designed for your spiritual focus."
        AppLanguage.BANGLA -> "একটি শান্ত ও মার্জিত ইন্টারফেসে প্রতিদিনের নামাজ সময়মতো আদায় করার অভ্যাস গড়ে তুলুন।"
    }

    fun onboardingStep2Title(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "1-Tap Effortless Logging"
        AppLanguage.BANGLA -> "সহজ এক ট্যাপে নামাজ ট্র্যাক করুন"
    }

    fun onboardingStep2Desc(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Mark your 5 daily prayers as On Time, Late, or Missed in seconds. Watch your streaks and completion grow day by day."
        AppLanguage.BANGLA -> "প্রতিটি নামাজের অবস্থা (যথাসময়ে, দেরিতে বা কাযা) চিহ্নিত করুন এবং আপনার ধারাবাহিকতা বজায় রাখুন।"
    }

    fun onboardingStep3Title(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Islamic Events & Reflections"
        AppLanguage.BANGLA -> "দিনপঞ্জিকা ও দোয়া-আমলের নোট"
    }

    fun onboardingStep3Desc(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Track blessed Islamic nights, Ramadan, Eid dates, and write personal reflections or Ayahs connected to your prayers."
        AppLanguage.BANGLA -> "মহিমান্বিত রাত, রোজা ও বিশেষ ইসলামিক দিবস মনে রাখুন এবং নামাজের সাথে প্রিয় দোয়া ও নোট লিখে রাখুন।"
    }

    fun onboardingStep4Title(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "100% Private & On-Device"
        AppLanguage.BANGLA -> "সম্পূর্ণ নিরাপদ ও অফলাইন"
    }

    fun onboardingStep4Desc(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Your spiritual journey is strictly between you and your Creator. No accounts, no ads, and no data ever leaves your phone."
        AppLanguage.BANGLA -> "আপনার ইবাদত শুধুই আপনার ও মহান রবের মাঝে। কোনো সাইন-আপের প্রয়োজন নেই, ডাটা থাকবে শুধুই আপনার ডিভাইসে।"
    }

    // Salat Report & Analytics
    fun salatReportBtn(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Report"
        AppLanguage.BANGLA -> "পরিসংখ্যান"
    }

    fun salatReportTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Salat Analytics & Report"
        AppLanguage.BANGLA -> "সালাতের হিসাব ও পরিসংখ্যান"
    }

    fun salatReportSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Filter by month & year for total rakats and wakto breakdown"
        AppLanguage.BANGLA -> "মাস ও বছর অনুযায়ী আদায়কৃত মোট রাকাত ও ওয়াক্তের হিসাব"
    }

    fun filterYearLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Select Year"
        AppLanguage.BANGLA -> "বছর নির্বাচন"
    }

    fun filterMonthLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Select Month"
        AppLanguage.BANGLA -> "মাস নির্বাচন"
    }

    fun allYears(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "All Years"
        AppLanguage.BANGLA -> "সকল বছর"
    }

    fun allMonths(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "All Months"
        AppLanguage.BANGLA -> "সকল মাস"
    }

    fun totalWaktoCount(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Total Prayers Performed"
        AppLanguage.BANGLA -> "আদায়কৃত মোট ওয়াক্ত"
    }

    fun totalRakatCount(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Total Rakats Performed"
        AppLanguage.BANGLA -> "আদায়কৃত মোট রাকাত"
    }

    fun rakatUnit(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Rakats"
        AppLanguage.BANGLA -> "রাকাত"
    }

    fun waktoUnit(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Prayers"
        AppLanguage.BANGLA -> "ওয়াক্ত"
    }

    fun onTimeCountLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "On Time"
        AppLanguage.BANGLA -> "যথাসময়ে"
    }

    fun lateCountLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Late"
        AppLanguage.BANGLA -> "দেরিতে"
    }

    fun qazaCountLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Qaza / Missed"
        AppLanguage.BANGLA -> "কাযা"
    }

    fun prayerBreakdownTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Prayer Wakto Performance"
        AppLanguage.BANGLA -> "ওয়াক্তভিত্তিক বিস্তারিত হিসাব"
    }

    // Event Report & Analytics
    fun eventReportBtn(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Report"
        AppLanguage.BANGLA -> "পরিসংখ্যান"
    }

    fun eventReportTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Event Analytics & Report"
        AppLanguage.BANGLA -> "দিবস ও আমলের পরিসংখ্যান"
    }

    fun eventReportSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Filter by month & year for event & fasting breakdown"
        AppLanguage.BANGLA -> "মাস ও বছর অনুযায়ী ইসলামিক দিবস ও বিশেষ আমলের হিসাব"
    }

    fun totalEventsCountLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Total Events"
        AppLanguage.BANGLA -> "মোট দিবস"
    }

    fun recurringEventsLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Yearly Recurring"
        AppLanguage.BANGLA -> "বাৎসরিক পুনরাবৃত্ত"
    }

    fun categoryBreakdownTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Category Breakdown"
        AppLanguage.BANGLA -> "ক্যাটাগরি অনুযায়ী দিবস"
    }

    fun eventsListTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Filtered Events List"
        AppLanguage.BANGLA -> "নির্বাচিত সময়সীমার দিবসসমূহ"
    }

    fun noReportEventsMsg(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "No events found for the selected month and year."
        AppLanguage.BANGLA -> "নির্বাচিত মাস ও বছরে কোনো দিবস যুক্ত করা হয়নি।"
    }

    // Reflection Report & Analytics
    fun noteReportBtn(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Report"
        AppLanguage.BANGLA -> "পরিসংখ্যান"
    }

    fun noteReportTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Reflection Analytics & Report"
        AppLanguage.BANGLA -> "ভাবনা ও নোটের পরিসংখ্যান"
    }

    fun noteReportSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Monthly & yearly breakdown of spiritual reflections and duas"
        AppLanguage.BANGLA -> "মাস ও বছর অনুযায়ী আত্মিক ভাবনা ও দোয়ার পরিসংখ্যান"
    }

    fun linkedToPrayerCountLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Linked to Prayer"
        AppLanguage.BANGLA -> "নামাজের সাথের নোট"
    }

    fun totalTagsCountLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Tags Used"
        AppLanguage.BANGLA -> "ব্যবহৃত ট্যাগ"
    }

    fun notesListTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Filtered Reflections & Duas"
        AppLanguage.BANGLA -> "নির্বাচিত সময়সীমার নোট ও দোয়া"
    }

    fun noReportNotesMsg(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "No reflections found for the selected month and year."
        AppLanguage.BANGLA -> "নির্বাচিত মাস ও বছরে কোনো নোট বা চিন্তা যুক্ত করা হয়নি।"
    }

    // Share App
    fun shareAppTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Share App"
        AppLanguage.BANGLA -> "অ্যাপ শেয়ার করুন"
    }

    fun shareAppSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Share with QR code & Play Store link"
        AppLanguage.BANGLA -> "কিউআর কোড ও প্লে স্টোর লিঙ্ক দিয়ে অন্যদের মাঝে শেয়ার করুন"
    }
}

