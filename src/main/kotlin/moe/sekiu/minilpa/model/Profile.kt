package moe.sekiu.minilpa.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonNames
import moe.sekiu.minilpa.setting
import java.nio.charset.Charset
import kotlin.io.encoding.Base64

object NicknameSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("profileNicknameB64")

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(Base64.Default.encode(value.toByteArray(Charsets.UTF_8)))
    }

    override fun deserialize(decoder: Decoder): String {
        var decodeStr: String
        val rawBytes = Base64.Default.decode(decoder.decodeString())
        decodeStr = String(rawBytes, Charset.forName(setting.`nickname-charset`))
        return decodeStr
    }
}

@Serializable
data class Profile(
    val iccid : String,
    val isdpAid : String,
    @SerialName("profileState")
    val state : State,
    @Serializable(NicknameSerializer::class)
    @SerialName("profileNicknameB64")
    val nickname : String?,
    val serviceProviderName : String?,
    @SerialName("profileName")
    val name : String?,
    val iconType : IconType?,
    val icon : String?,
    @SerialName("profileClass")
    val `class` : Class
)
{
    @Serializable
    enum class State
    {
        @SerialName("enabled")
        ENABLED,
        @SerialName("disabled")
        DISABLED
    }

    @Serializable
    enum class IconType
    {
        @SerialName("jpg")
        @JsonNames("jpeg")
        JPG,
        @SerialName("png")
        PNG
    }

    @Serializable
    enum class Class
    {
        @SerialName("test")
        TEST,
        @SerialName("provisioning")
        PROVISIONING,
        @SerialName("operational")
        OPERATIONAL,
        @SerialName("unknown")
        UNKNOWN
    }
}