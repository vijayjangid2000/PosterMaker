# PosterMaker
Poster making application

This apps converts images to posters for advertisements purposes.

App Flow:

1. User select image from local storage.
2. User set the information in the UserDetailsActivity.
3. Click CreatePoster -> user can share that poster as a png to other users.
4. That poster get saved in downloads folder.

You select image like this below:

![Before](https://bn1305files.storage.live.com/y4mEQcW1oQmvpPoDT-6Er5yivahHFMLG6eiPLgx3Y_ork9giodZixoIhZCw491Z4uTs_tkgLRRoY9jpGtKl1u6Y4_G1vJIUjgqU9UP6rfH-UhtlTuEa7Wcz24fqDEx_quhsHY4HvYyqqh5bOgVUuLIT9irzZW8os-xxPat0LxyrivnwrA611I34D2O4Lu-mG0IpiLDF0NyB5A6PU4DTNiA9LQ/images.jpeg?psid=1&width=339&height=226)

It gets converted like this:

![After](https://bn1305files.storage.live.com/y4mKLEv55L4HGdOOBp255UMr3wL-n8c0xUkcBstinV28C6kXwz-E4tBhzikS6hVd5MjyhIANFzazNGd-kSY9MGEk_DYz8LT4WU_8L0Vbqm6liZZx8wjOC7o_WGb2wk2pIVIpFg9PAJlxL3fCNOaqESpOsUByg8u2atkzp8_UeseF7pTl-SUzyCogPx9cyXudk5kaXc_I8pZ-37tTHyUlY8SYg/createdPoster.png?psid=1&width=380&height=235)

Usage:

There is a poster Class, All you need to do is:

Poster poster = new Poster('filepaths'); // check the constructor
poster.getLatestBitmap(); // it return the bitmap
