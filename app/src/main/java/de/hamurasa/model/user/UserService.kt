package de.hamurasa.model.user
/*
import de.hamurasa.model.lesson.LessonService
import de.hamurasa.model.profile.ProfileService
import io.reactivex.Observable

interface UserService {
    fun findById(id: Long): Observable<User?>

    fun findByName(name: String): Observable<User>?

    fun findAll(): Observable<List<User>>

    fun save(user: User): Observable<User>?

    fun delete(user: UserDTO)

    fun convertToDto(user: User): UserDTO

    fun convertDtoToEntity(user: UserDTO): Observable<User>?

}


class UserServiceImpl(
    private val userRepository: UserRepository,
    private val profileService: ProfileService
) : UserService {

    override fun findById(id: Long): Observable<User?> {
        return userRepository.findById(id)
    }

    override fun findByName(name: String): Observable<User>? {
        return userRepository.findByName(name)
    }

    override fun findAll(): Observable<List<User>> {
        return userRepository.findAll()
    }

    override fun save(user: User): Observable<User>? {
        val name = user.name
        val oldUser = findByName(name)
        if (oldUser != null)
            return oldUser

        userRepository.save(user)
        return userRepository.findByName(name)
    }

    override fun delete(user: UserDTO) {
        val observable = convertDtoToEntity(user) ?: return
        userRepository.delete(observable.blockingFirst())
    }


    override fun convertDtoToEntity(user: UserDTO): Observable<User>? {
        return save(User(user.name))
    }

    override fun convertToDto(user: User): UserDTO {
        return UserDTO(
            id = user.id,
            name = user.name,
            profiles = listOf()
        )
    }


}*/