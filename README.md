Who Said?
=========
![.github/workflows/main.yml](https://github.com/bharatvaj/Quoter/workflows/.github/workflows/main.yml/badge.svg)

'Who Said?' is a quote browser for android. Browse literally thousands of quotes with a single swipe.

Quick Overview
--------------

service/QuoteDownloader.kt
--------------------------
Download quotes and sync them with the local database. Use paging library maybe? Should we handle all CRUD operations? How to handle sync correctly?

model/Quote.kt
--------------

Quote
-----
* Quote string is atmost 280 characters wide. Rationale: If it fits in a tweet, it's good enough to be a quote.

Quote {
	id: ULong,
	quote: String,
	authorId: ULong,
	tagIds: ULongArray
}

Author
------
* Author name is at most 100 characters wide.

Author {
	id: ULong,
	authorName: String,
}

Tag
---
* Tag length is atmost 100 characters.

Tag {
	id: ULong,
	tagName: String
}

service/ImageProvider.kt
------------------------

QuoteViewCharacteristics
