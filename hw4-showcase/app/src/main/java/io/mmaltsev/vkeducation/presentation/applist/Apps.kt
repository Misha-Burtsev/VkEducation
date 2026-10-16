package io.mmaltsev.vkeducation.presentation.applist

import io.mmaltsev.vkeducation.presentation.appdetails.Category

// Захардкоженные данные. В будущем заменим на вызов API.
fun getApps(): List<App> = listOf(
    App(
        name = "QuickNote",
        developer = "Quick Apps",
        category = Category.APP,
        iconUrl = "https://fastly.picsum.photos/id/237/200/200.jpg?hmac=zHUGikXUDyLCCmvyww1izLK3R3k8oRYBRiTizZEdyfI",
    ),
    App(
        name = "Calmalist",
        developer = "Calm Studio",
        category = Category.APP,
        iconUrl = "https://fastly.picsum.photos/id/238/200/200.jpg?hmac=O4Jc6lqHVfaKVzLf8bWssNTbWzQoaRUC0TDXod9xDdM",
    ),
    App(
        name = "FitRiver",
        developer = "River Labs",
        category = Category.APP,
        iconUrl = "https://fastly.picsum.photos/id/239/200/200.jpg?hmac=8JqlXUpZ9Xy0H6tMK8sCPQAYU9vUn9Qa8Kg-U9h3sCY",
    ),
    App(
        name = "TaskLoop",
        developer = "Loop Team",
        category = Category.APP,
        iconUrl = "https://fastly.picsum.photos/id/240/200/200.jpg?hmac=PZiJibMUuv5Zpv9MAcqLaCYJOPt-XNuDh3RPBZiGcoY",
    ),
    App(
        name = "Pixtrix",
        developer = "Pix Games",
        category = Category.GAME,
        iconUrl = "https://fastly.picsum.photos/id/241/200/200.jpg?hmac=F-mDYyK1xUiXjlLd5PvE8EQpTwGfFndV7mMaANDtl28",
    ),
    App(
        name = "TinyChef",
        developer = "Tiny Games",
        category = Category.GAME,
        iconUrl = "https://fastly.picsum.photos/id/242/200/200.jpg?hmac=Z3aa8zbEQkEMFgnVh0Pn96vmCZHhJ17qzCrePYksrcY",
    ),
    App(
        name = "GymMate",
        developer = "Mate Apps",
        category = Category.APP,
        iconUrl = "https://fastly.picsum.photos/id/243/200/200.jpg?hmac=fW5ZwzzyTBy2t2MROp988oq12mZnKwN0coFLhZEE87s",
    ),
    App(
        name = "FlowCard",
        developer = "Flow Inc.",
        category = Category.APP,
        iconUrl = "https://fastly.picsum.photos/id/244/200/200.jpg?hmac=Q1gdvE6ZPZUX3nXkxvmzuc12eKVZ9XVEmSH3nCJ2OOo",
    ),
    App(
        name = "DreamSafe",
        developer = "Dream Games",
        category = Category.GAME,
        iconUrl = "https://fastly.picsum.photos/id/2/200/200.jpg?hmac=isSWZUpv7D1D156XcADPOCZlfCG9mmvb8OlXFdvLdK0",
    ),
)
