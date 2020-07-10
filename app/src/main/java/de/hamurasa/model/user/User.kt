package de.hamurasa.model.user

import de.hamurasa.model.profile.Profile
import de.hamurasa.model.profile.ProfileDTO
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany


@Entity
data class User(
    var name: String = "User"
) {
    lateinit var profiles: ToMany<Profile>

    @Id
    var id: Long = 0
}


data class UserDTO(
    var id: Long = 0,
    var name : String = "User",
    var profiles: List<ProfileDTO>
)