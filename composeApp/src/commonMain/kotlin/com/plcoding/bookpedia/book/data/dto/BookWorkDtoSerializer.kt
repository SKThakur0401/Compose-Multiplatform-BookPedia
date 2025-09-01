package com.plcoding.bookpedia.book.data.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement

@OptIn(ExperimentalSerializationApi::class)
object BookWorkDtoSerializer: KSerializer<BookWorkDto> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
        BookWorkDto::class.simpleName!!
    ) {
        element<String?>("description")
        element<List<String>?>("subjects")
        element<List<String>?>("subject_people")
        element<List<String>?>("subject_places")
        element<List<String>?>("subject_times")
        element<List<ExcerptDto>?>("excerpts")
        element<List<LinkDto>?>("links")
        element<String?>("title")
        element<String?>("key")
        element<List<AuthorDto>?>("authors")
        element<String?>("first_publish_date")
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): BookWorkDto = decoder.decodeStructure(descriptor) {
        var description: String? = null
        var subjects: List<String>? = null
        var subjectPeople: List<String>? = null
        var subjectPlaces: List<String>? = null
        var subjectTimes: List<String>? = null
        var excerpts: List<ExcerptDto>? = null
        var links: List<LinkDto>? = null
        var title: String? = null
        var key: String? = null
        var authors: List<AuthorDto>? = null
        var firstPublishDate: String? = null

        while(true) {
            when(val index = decodeElementIndex(descriptor)) {
                0 -> {
                    val jsonDecoder = decoder as? JsonDecoder ?: throw SerializationException(
                        "This decoder only works with JSON."
                    )
                    val element = jsonDecoder.decodeJsonElement()
                    description = if(element is JsonObject) {
                        decoder.json.decodeFromJsonElement<DescriptionDto>(
                            element = element,
                            deserializer = DescriptionDto.serializer()
                        ).value
                    } else if(element is JsonPrimitive && element.isString) {
                        element.content
                    } else null
                }
                1 -> subjects = decodeNullableSerializableElement(descriptor, 1, ListSerializer(String.serializer()))
                2 -> subjectPeople = decodeNullableSerializableElement(descriptor, 2, ListSerializer(String.serializer()))
                3 -> subjectPlaces = decodeNullableSerializableElement(descriptor, 3, ListSerializer(String.serializer()))
                4 -> subjectTimes = decodeNullableSerializableElement(descriptor, 4, ListSerializer(String.serializer()))
                5 -> excerpts = decodeNullableSerializableElement(descriptor, 5, ListSerializer(ExcerptDto.serializer()))
                6 -> links = decodeNullableSerializableElement(descriptor, 6, ListSerializer(LinkDto.serializer()))
                7 -> title = decodeNullableSerializableElement(descriptor, 7, String.serializer())
                8 -> key = decodeNullableSerializableElement(descriptor, 8, String.serializer())
                9 -> authors = decodeNullableSerializableElement(descriptor, 9, ListSerializer(AuthorDto.serializer()))
                10 -> firstPublishDate = decodeNullableSerializableElement(descriptor, 10, String.serializer())
                CompositeDecoder.DECODE_DONE -> break
                else -> throw SerializationException("Unexpected index $index")
            }
        }

        return@decodeStructure BookWorkDto(
            description = description,
            subjects = subjects,
            subjectPeople = subjectPeople,
            subjectPlaces = subjectPlaces,
            subjectTimes = subjectTimes,
            excerpts = excerpts,
            links = links,
            title = title,
            key = key,
            authors = authors,
            firstPublishDate = firstPublishDate
        )
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: BookWorkDto) = encoder.encodeStructure(
        descriptor
    ) {
        value.description?.let { encodeStringElement(descriptor, 0, it) }
        value.subjects?.let { encodeSerializableElement(descriptor, 1, ListSerializer(String.serializer()), it) }
        value.subjectPeople?.let { encodeSerializableElement(descriptor, 2, ListSerializer(String.serializer()), it) }
        value.subjectPlaces?.let { encodeSerializableElement(descriptor, 3, ListSerializer(String.serializer()), it) }
        value.subjectTimes?.let { encodeSerializableElement(descriptor, 4, ListSerializer(String.serializer()), it) }
        value.excerpts?.let { encodeSerializableElement(descriptor, 5, ListSerializer(ExcerptDto.serializer()), it) }
        value.links?.let { encodeSerializableElement(descriptor, 6, ListSerializer(LinkDto.serializer()), it) }
        value.title?.let { encodeStringElement(descriptor, 7, it) }
        value.key?.let { encodeStringElement(descriptor, 8, it) }
        value.authors?.let { encodeSerializableElement(descriptor, 9, ListSerializer(AuthorDto.serializer()), it) }
        value.firstPublishDate?.let { encodeStringElement(descriptor, 10, it) }
    }
}