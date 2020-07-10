package de.hamurasa.model.user
/*
import de.hamurasa.data.ObjectBox
import io.objectbox.Box
import io.objectbox.rx.RxQuery
import io.reactivex.Observable
import io.reactivex.Single


interface UserRepository {
    fun findById(id: Long): Observable<User?>

    fun findByName(name: String): Observable<User>?

    fun findAll(): Observable<List<User>>

    fun save(user: User)

    fun delete(user: User)
}


class UserRepositoryImpl : UserRepository {

    private var userBox: Box<User> = ObjectBox.boxStore.boxFor(
        User::class.java
    )

    override fun findById(id: Long): Observable<User?> {
        return Observable.just(userBox.query().equal(User_.id, id).build().findFirst())
    }

    override fun findByName(name: String): Observable<User>? {
        return Observable.just(userBox.query().equal(User_.id, name).build().findFirst())
    }

    override fun findAll(): Observable<List<User>> {
        return RxQuery.observable(userBox.query().build())
    }

    override fun save(user: User) {
        userBox.put(user)
    }

    override fun delete(user: User) {
        userBox.remove(user)
    }

}
*/
