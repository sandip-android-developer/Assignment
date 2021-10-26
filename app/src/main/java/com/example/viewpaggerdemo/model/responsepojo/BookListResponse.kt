package com.example.viewpaggerdemo.model.responsepojo


import com.google.gson.annotations.SerializedName

open class BookListResponse {
    @SerializedName("copyright")
    var copyright: String? = ""

    @SerializedName("last_modified")
    var lastModified: String? = ""

    @SerializedName("num_results")
    var numResults: Int? = 0

    @SerializedName("results")
    var results: List<Result>? = listOf()

    @SerializedName("status")
    var status: String? = ""

    open class Result {
        @SerializedName("amazon_product_url")
        var amazonProductUrl: String? = ""

        @SerializedName("asterisk")
        var asterisk: Int? = 0

        @SerializedName("bestsellers_date")
        var bestsellersDate: String? = ""

        @SerializedName("book_details")
        var bookDetails: List<BookDetail>? = listOf()

        @SerializedName("dagger")
        var dagger: Int? = 0

        @SerializedName("display_name")
        var displayName: String? = ""

        @SerializedName("isbns")
        var isbns: List<Isbn>? = listOf()

        @SerializedName("list_name")
        var listName: String? = ""

        @SerializedName("published_date")
        var publishedDate: String? = ""

        @SerializedName("rank")
        var rank: Int? = 0

        @SerializedName("rank_last_week")
        var rankLastWeek: Int? = 0

        @SerializedName("reviews")
        var reviews: List<Review>? = listOf()

        @SerializedName("weeks_on_list")
        var weeksOnList: Int? = 0

        open class BookDetail {
            @SerializedName("age_group")
            var ageGroup: String? = ""

            @SerializedName("author")
            var author: String? = ""

            @SerializedName("contributor")
            var contributor: String? = ""

            @SerializedName("contributor_note")
            var contributorNote: String? = ""

            @SerializedName("description")
            var description: String? = ""

            @SerializedName("price")
            var price: Int? = 0

            @SerializedName("primary_isbn1")
            var primaryIsbn1: String? = ""

            @SerializedName("primary_isbn13")
            var primaryIsbn13: String? = ""

            @SerializedName("publisher")
            var publisher: String? = ""

            @SerializedName("title")
            var title: String? = ""
        }

        open class Isbn {
            @SerializedName("isbn1")
            var isbn1: String? = ""

            @SerializedName("isbn13")
            var isbn13: String = ""
        }

        open class Review {
            @SerializedName("article_chapter_link")
            var articleChapterLink: String? = ""

            @SerializedName("book_review_link")
            var bookReviewLink: String? = ""

            @SerializedName("first_chapter_link")
            var firstChapterLink: String? = ""

            @SerializedName("sunday_review_link")
            var sundayReviewLink: String = ""
        }

    }
}
