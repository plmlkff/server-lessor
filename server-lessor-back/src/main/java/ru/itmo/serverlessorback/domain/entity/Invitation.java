package ru.itmo.serverlessorback.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.itmo.serverlessorback.domain.entity.enums.InvitationStatus;

import java.util.UUID;

import static ru.itmo.serverlessorback.domain.entity.Invitation.TABLE_NAME;

@Data
@Entity
@Table(name = TABLE_NAME)
public class Invitation {
    public static final String TABLE_NAME = "invitation";

    @EmbeddedId
    private Key id;

    @Enumerated(EnumType.STRING)
    @Column
    private InvitationStatus status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referee_id")
    @MapsId("refereeId")
    private User referee;

    @ManyToOne
    @JoinColumn(name = "referral_id")
    @MapsId("referralId")
    private User referral;

    @Embeddable
    @Data
    public static class Key {
        private UUID refereeId;

        private UUID referralId;
    }
}
