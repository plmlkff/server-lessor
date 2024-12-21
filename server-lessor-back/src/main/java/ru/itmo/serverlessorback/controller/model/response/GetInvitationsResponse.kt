package ru.itmo.serverlessorback.controller.model.response;

import ru.itmo.serverlessorback.domain.entity.Invitation
import ru.itmo.serverlessorback.domain.entity.enums.InvitationStatus

data class GetInvitationsResponse(
    val referralUsername: String,
    val status: InvitationStatus,
) {
    companion object {
        fun fromDomain(invitation: Invitation) =
            GetInvitationsResponse(
                referralUsername = invitation.referral.login,
                status = invitation.status,
            )
    }
}
