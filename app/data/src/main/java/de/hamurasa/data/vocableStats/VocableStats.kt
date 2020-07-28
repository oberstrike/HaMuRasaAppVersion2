package de.hamurasa.data.vocableStats

import de.hamurasa.data.util.DateTimeStringConverter
import de.hamurasa.data.vocable.Vocable
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import org.joda.time.DateTime

@Entity
data class VocableStats(
    @Id var id: Long = 0,
    var repetitions: Long = 0,
    @Convert(converter = DateTimeStringConverter::class, dbType = String::class)
    var lastRepetitionDate: DateTime = DateTime.now(),
    @Convert(converter = DateTimeStringConverter::class, dbType = String::class)
    var nextRepetitionDate: DateTime = DateTime.now()
) {
    lateinit var vocable: ToOne<Vocable>
}