package de.hamurasa.data.vocable

import de.hamurasa.data.AbstractObjectBoxTest

class VocableBoxTest : AbstractObjectBoxTest<Vocable>() {

    override fun useClass(): Class<Vocable> = Vocable::class.java

}