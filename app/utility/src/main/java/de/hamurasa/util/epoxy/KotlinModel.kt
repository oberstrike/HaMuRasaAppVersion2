package de.hamurasa.util.epoxy

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.airbnb.epoxy.EpoxyModel
import java.lang.Exception
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * A pattern for using epoxy models with Kotlin with no annotations or code generation.
 *
 * See [com.airbnb.epoxy.kotlinsample.models.ItemDataClass] for a usage example.
 */
abstract class KotlinModel(
    @LayoutRes private val layoutRes: Int
) : EpoxyModel<View>() {

    companion object {
        private val cache: MutableMap<Int, MutableList<Int>> = mutableMapOf()
    }

    var view: View? = null

    abstract fun bind()

    override fun bind(view: View) {
        this.view = view
        if(!cache.containsKey(layoutRes))
            cache[layoutRes] = mutableListOf()
        bind()
    }


    override fun unbind(view: View) {
        this.view = null
    }

    override fun getDefaultLayout() = layoutRes


    protected fun <V : View> bind(@IdRes id: Int) = object : ReadOnlyProperty<KotlinModel, V> {

        override fun getValue(thisRef: KotlinModel, property: KProperty<*>): V {

            val listOfIds = cache[layoutRes] ?: throw Exception("Error")

            if (listOfIds.contains(id))
                return view?.findViewById(id) as V

            val item = view?.findViewById(id) as V?
                ?: throw IllegalStateException("View ID $id for '${property.name}' not found.")

            listOfIds.add(id)

            return item

        }
    }
}