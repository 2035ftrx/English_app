package com.example.learningenglish.main

import com.example.learningenglish.R


sealed interface IMainTabs {

    fun title(): String
    fun icon(): Int

    data object Home : IMainTabs {
        override fun title(): String {
            return "Home"
        }

        override fun icon(): Int {
            return R.drawable.ic_book_24
        }
    }

    data object Statistics : IMainTabs {
        override fun title(): String {
            return "Statistics"
        }

        override fun icon(): Int {
            return R.drawable.ic_bar_chart_24
        }
    }

    data object Setting : IMainTabs {
        override fun title(): String {
            return "Setting"
        }

        override fun icon(): Int {
            return R.drawable.ic_settings_24
        }
    }

}