package layout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoordinatorMemberResponse(
    val CommunityBelong: String?,
    val CoordinatorId: Int?,
    val CoordinatorNumber: String?,
    val CountryId: Int?,
    val CreatedDate: String?,
    val Createdstring: String?,
    val CustomerImagePath: String?,
    val EmailId: String?,
    val FirstName: String?,
    val Gender: Int?,
    val GeoAddress: String?,
    val HelpLineNumber: String?,
    val IsCoordinator: Int?,
    val IsFriend: Int?,
    val LastName: String?,
    val Latitude: Double?,
    val Location: String?,
    val LocationId: Int?,
    val Longitude: Double?,
    val MemberId: Int?,
    val MemberInfo: String?,
    val Mobile: String?,
    val ModifiedDate: String?,
    val Modifiedstring: String?,
    val OrganisationName: String?,
    val Pin: String?,
    val PostCode: String?,
    val ProfilePic: String?,
    val ReferredById: Int?,
    val RequestSent: Int?,
    val RequestStatus: String?,
    val Status: Int?,
    val TotalRecords: Int?,
    val UserId: String?,
    val UserType: String?
) : Parcelable