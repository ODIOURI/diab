/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.core.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.diab.core.data.converters.TimeFrameConverter

@Entity(tableName = "insulin")
class Insulin {

    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0

    @ColumnInfo(name = "name")
    var name: String = ""
    @ColumnInfo(name = "timeFrame")
    @TypeConverters(TimeFrameConverter::class)
    var timeFrame: TimeFrame = TimeFrame.EXTRA
    @ColumnInfo(name = "isBasal")
    var isBasal: Boolean = false
    @ColumnInfo(name = "hasHalfUnits")
    var hasHalfUnits: Boolean = false

    @Ignore
    constructor()

    constructor(
        uid: Long,
        name: String,
        timeFrame: TimeFrame,
        isBasal: Boolean,
        hasHalfUnits: Boolean
    ) {
        this.uid = uid
        this.name = name
        this.timeFrame = timeFrame
        this.isBasal = isBasal
        this.hasHalfUnits = hasHalfUnits
    }

    fun setTimeFrame(timeFrame: Int) {
        this.timeFrame = TimeFrameConverter().toTimeFrame(timeFrame)
    }

    fun getDisplayedString(value: Float) = "%1\$s %2\$.1f".format(name, value)

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Insulin) {
            return false
        }

        return other.name == name && other.timeFrame.toInt() == timeFrame.toInt()
    }

    override fun hashCode() = super.hashCode() + 1

    override fun toString() = "$name: $uid, ${timeFrame.toInt()}, $isBasal, $hasHalfUnits"
}
