package dev.inmo.krontab

import dev.inmo.krontab.internal.*

/**
 * @see createSimpleScheduler
 * @see buildSchedule
 */
typealias KrontabTemplate = String

/**
 * Parse [incoming] string and adapt according to next format: "* * * * *" where order of things:
 *
 * * seconds
 * * minutes
 * * hours
 * * dayOfMonth
 * * month
 * * (optional) year
 *
 * And each one have next format:
 *
 * `{number}[,{number},...]` or `*`
 *
 * and {number} here is one of
 *
 * * {int}-{int}
 * * {int}/{int}
 * * *&#47;{int}
 * * {int}
 * * F
 * * L
 *
 * Additional info about ranges can be found in follow accordance:
 *
 * * Seconds ranges can be found in [secondsRange]
 * * Minutes ranges can be found in [minutesRange]
 * * Hours ranges can be found in [hoursRange]
 * * Days of month ranges can be found in [dayOfMonthRange]
 * * Months ranges can be found in [monthRange]
 * * Years ranges can be found in [yearRange] (in fact - any [Int])
 *
 * Examples:
 *
 * * "0/5 * * * *" for every five seconds triggering
 * * "0/5,L * * * *" for every five seconds triggering and on 59 second
 * * "0/15 30 * * *" for every 15th seconds in a half of each hour
 * * "1 2 3 F,4,L 5" for triggering in near first second of second minute of third hour of fourth day of may
 * * "1 2 3 F,4,L 5 2021" for triggering in near first second of second minute of third hour of fourth day of may of 2021st year
 *
 * @see dev.inmo.krontab.internal.createKronScheduler
 */
fun createSimpleScheduler(incoming: KrontabTemplate): KronScheduler {
    val yearSource: String?
    val (secondsSource, minutesSource, hoursSource, dayOfMonthSource, monthSource) = incoming.split(" ").also {
        yearSource = it.getOrNull(5)
    }

    val secondsParsed = parseSeconds(secondsSource)
    val minutesParsed = parseMinutes(minutesSource)
    val hoursParsed = parseHours(hoursSource)
    val dayOfMonthParsed = parseDaysOfMonth(dayOfMonthSource)
    val monthParsed = parseMonths(monthSource)
    val yearParsed = parseYears(yearSource)

    return createKronScheduler(
        secondsParsed, minutesParsed, hoursParsed, dayOfMonthParsed, monthParsed, yearParsed
    )
}

/**
 * Shortcut for [createSimpleScheduler]
 */
fun buildSchedule(incoming: KrontabTemplate): KronScheduler = createSimpleScheduler(incoming)

/**
 * Shortcut for [buildSchedule]
 */
fun KrontabTemplate.toSchedule(): KronScheduler = buildSchedule(this)

/**
 * Shortcut for [buildSchedule]
 */
fun KrontabTemplate.toKronScheduler(): KronScheduler = buildSchedule(this)