package com.example.shoestore.data.mapper

fun categoryNameFromId(id: String?): String = when (id) {
    "ea4ed603-8cbe-4d57-a359-b6b843a645bc" -> "Outdoor"
    "4f3a690b-41bf-4fca-8ffc-67cc385c6637" -> "Tennis"
    "76ab9d74-7d5b-4dee-9c67-6ed4019fa202" -> "Men"
    "8143b506-d70a-41ec-a5eb-3cf09627da9e" -> "Women"
    else -> "All"
}
