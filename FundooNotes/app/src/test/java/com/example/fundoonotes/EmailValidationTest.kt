package com.example.fundoonotes

import com.example.fundoonotes.model.AuthListener
import com.example.fundoonotes.model.User
import com.example.fundoonotes.model.UserAuthService
import com.example.fundoonotes.view.FragmentRegister
import com.example.fundoonotes.viewmodel.LoginViewModel
import com.example.fundoonotes.viewmodel.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class EmailValidationTest {
    //val userAuthService: UserAuthService = UserAuthService()

    @Test
    fun emailReturnsFalseWhenEmailIsEmpty(){
        val name =  ""
        val email = ""
        val password = ""
        val repeatPassword = ""
        val fragmentRegister: FragmentRegister = FragmentRegister()
        assertEquals(false, fragmentRegister.isValidEmail(email))
    }

    @Test
    fun passwordReturnsFalseWhenPasswordIsEmpty(){
        val name =  ""
        val email = ""
        val password = ""
        val repeatPassword = ""
        val fragmentRegister: FragmentRegister = FragmentRegister()
        assertEquals(false, fragmentRegister.isValidPassword(password))
    }

    @Test
    fun incorrectEmailReturnsFalseWhenEmailIsInvalid(){
        val name =  ""
        val email = "capt.ing@gmail"
        val password = ""
        val repeatPassword = ""
        val fragmentRegister: FragmentRegister = FragmentRegister()
        assertEquals(false, fragmentRegister.isValidEmail(email))
    }


    @Test
    fun IfPasswordDoNotContainASpecialCharacterOrCapslockReturnsFalse(){
        val name =  ""
        val email = ""
        val password = "1234567"
        val repeatPassword = ""
        val fragmentRegister: FragmentRegister = FragmentRegister()
        assertEquals(false, fragmentRegister.isValidPassword(password))
    }

    @Test
    fun IfPasswordIsLessThanSixDigitsPasswordReturnsFalse(){
        val email = ""
        val password = ""
        val listener = ""
        val fragmentRegister: FragmentRegister = FragmentRegister()
        assertEquals(false, fragmentRegister.isValidPassword(password))
    }
}