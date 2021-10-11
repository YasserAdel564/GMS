package com.gms.app.ui.auth.signUp

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gms.app.R
import com.gms.app.data.storage.local.PreferencesHelper
import com.gms.app.data.storage.remote.model.auth.CountryModel
import com.gms.app.data.storage.remote.model.auth.GenderModel
import com.gms.app.data.storage.remote.model.auth.NationalityModel
import com.gms.app.data.storage.remote.model.auth.SignUpBody
import com.gms.app.databinding.SignUpFragmentBinding
import com.gms.app.databinding.SplashFragmentBinding
import com.gms.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {


    private lateinit var binding: SignUpFragmentBinding
    private val signUpVM: SignUpVM by viewModels()

    private var countriesArray = ArrayList<CountryModel>()
    private var livingArray = ArrayList<CountryModel>()
    private var genderArray = ArrayList<GenderModel>()
    private var nationalitiesArray = ArrayList<NationalityModel>()
    private val calendar = Calendar.getInstance()

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SignUpFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpVM.storedCountries.observe(viewLifecycleOwner, { renderCountriesData(it) })
        signUpVM.storedGender.observe(viewLifecycleOwner, { renderGendersData(it) })
        signUpVM.storedNationalities.observe(viewLifecycleOwner, { renderNationalitiesData(it) })
        signUpVM.storedLiving.observe(viewLifecycleOwner, { renderLivingData(it) })
        signUpVM.uiState.observeEvent(viewLifecycleOwner, { onSignUpResponse(it) })
        setUpToolbar()
        initViewsClicks()
        onSpinnerSelected()
    }

    private fun initViewsClicks() {
        binding.toolbar.backImgToolbar.setOnClickListener { findNavController().navigateUp() }
        binding.dateOfBirthTxt.setOnClickListener { showDatePicker() }
        binding.signUpBtn.setOnClickListener { validation() }
    }

    private fun setUpToolbar() {
        binding.toolbar.titleTvToolbar.text =
            requireActivity().resources.getString(R.string.signUp_label)
    }

    private fun renderCountriesData(list: List<CountryModel>) {
        countriesArray.clear()
        countriesArray.add(
            0,
            CountryModel(0, requireActivity().resources.getString(R.string.select_country))
        )
        countriesArray.addAll(list)
        if (list.isNotEmpty()) {
            val spinnerArrayAdapter = MyArrayAdapter(requireContext(), countriesArray, true)
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_view)
            binding.countrySpinner.adapter = spinnerArrayAdapter
        }
    }

    private fun renderGendersData(list: List<GenderModel>) {
        genderArray.clear()
        genderArray.add(
            0,
            GenderModel(0, requireActivity().resources.getString(R.string.select_gender))
        )
        genderArray.addAll(list)
        if (list.isNotEmpty()) {
            val spinnerArrayAdapter = MyArrayAdapter(requireContext(), genderArray, true)
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_view)
            binding.genderSpinner.adapter = spinnerArrayAdapter
        }

    }

    private fun renderNationalitiesData(list: List<NationalityModel>) {
        nationalitiesArray.clear()
        nationalitiesArray.add(
            0,
            NationalityModel(0, requireActivity().resources.getString(R.string.select_nationality))
        )
        nationalitiesArray.addAll(list)
        if (list.isNotEmpty()) {
            val spinnerArrayAdapter = MyArrayAdapter(requireContext(), nationalitiesArray, true)
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_view)
            binding.nationalitySpinner.adapter = spinnerArrayAdapter
        }

    }

    private fun renderLivingData(list: List<CountryModel>) {
        livingArray.clear()
        livingArray.add(
            0,
            CountryModel(0, requireActivity().resources.getString(R.string.select_living))
        )
        livingArray.addAll(list)
        if (list.isNotEmpty()) {
            val spinnerArrayAdapter = MyArrayAdapter(requireContext(), livingArray, true)
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_view)
            binding.livingSpinner.adapter = spinnerArrayAdapter
        }

    }

    private fun onSpinnerSelected() {
        binding.countrySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0)
                        signUpVM.selectedCountry = countriesArray[position]
                }
            }

        binding.genderSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0)
                        signUpVM.selectedGender = genderArray[position]
                }
            }

        binding.nationalitySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0)
                        signUpVM.selectedNationality = nationalitiesArray[position]
                }
            }

        binding.livingSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0)
                        signUpVM.selectedLiving = livingArray[position]
                }
            }

    }

    private var date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        binding.dateOfBirthTxt.text = sdf.format(calendar.time)
        signUpVM.selectedDateOfBirth = sdf.format(calendar.time).toString()
    }

    private fun showDatePicker() {
        DatePickerDialog(
            requireActivity(), date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun validation() {
        val fullName = binding.fullNameEt.text.trim().toString()
        val email = binding.emailEt.text.trim().toString()
        val password = binding.passwordEt.text.trim().toString()
        val confirmPassword = binding.confirmPasswordEt.text.trim().toString()
        val phone = binding.phoneEt.text.trim().toString()
        val contact = binding.contactEt.text.trim().toString()
        val address = binding.addressEt.text.trim().toString()

        val university = binding.universityEt.text.trim().toString()
        val education = binding.educationEt.text.trim().toString()
        val graduationYear = binding.gradYearEt.text.trim().toString()
        val note = binding.notesEt.text.trim().toString()



        if (fullName.isBlank()) {
            binding.fullNameEt.error = getString(R.string.required_field)
            return
        }
        if (email.isBlank()) {
            binding.emailEt.error = getString(R.string.required_field)
            return
        }
        if (!email.isValidEmail()) {
            binding.emailEt.error = getString(R.string.email_validation)
            return
        }
        if (password.isBlank()) {
            binding.passwordEt.error = getString(R.string.required_field)
            return
        }
        if (password.length < 8) {
            binding.passwordEt.error = getString(R.string.password_validation)
            return
        }
        if (confirmPassword.isBlank()) {
            binding.confirmPasswordEt.error = getString(R.string.required_field)
            return
        }
        if (password != confirmPassword) {
            binding.confirmPasswordEt.error = getString(R.string.password_mis_match)
            return
        }
        if (phone.isBlank()) {
            binding.phoneEt.error = getString(R.string.required_field)
            return
        }
        if (contact.isBlank()) {
            binding.contactEt.error = getString(R.string.required_field)
            return
        }
        if (address.isBlank()) {
            binding.addressEt.error = getString(R.string.required_field)
            return
        }

        if (signUpVM.selectedDateOfBirth == null) {
            binding.dateOfBirthTxt.error = getString(R.string.required_field)
            return
        }
        if (signUpVM.selectedGender == null) {
            activity?.snackBar(
                requireActivity().getString(R.string.select_gender),
                binding.viewRoot
            )
            return
        }
        if (signUpVM.selectedCountry == null) {
            activity?.snackBar(
                requireActivity().getString(R.string.select_country),
                binding.viewRoot
            )
            return
        }

        if (signUpVM.selectedNationality == null) {
            activity?.snackBar(
                requireActivity().getString(R.string.select_nationality),
                binding.viewRoot
            )
            return
        }
        if (signUpVM.selectedLiving == null) {
            activity?.snackBar(
                requireActivity().getString(R.string.select_living),
                binding.viewRoot
            )
            return
        }

        if (university.isBlank()) {
            binding.universityEt.error = getString(R.string.required_field)
            return
        }
        if (education.isBlank()) {
            binding.educationEt.error = getString(R.string.required_field)
            return
        }
        if (graduationYear.isBlank()) {
            binding.gradYearEt.error = getString(R.string.required_field)
            return
        }
        if (note.isBlank()) {
            binding.notesEt.error = getString(R.string.required_field)
            return
        }

        val body = SignUpBody(
            fullName = fullName,
            birthDate = signUpVM.selectedDateOfBirth,
            email = email,
            phone = phone,
            contact = contact,
            genderId = signUpVM.selectedGender?.id!!,
            nationalityId = signUpVM.selectedNationality?.id!!,
            countryId = signUpVM.selectedCountry?.id!!,
            livingId = signUpVM.selectedLiving?.id!!,
            genderName = signUpVM.selectedGender?.name,
            nationalityName = signUpVM.selectedNationality?.name,
            livingName = signUpVM.selectedLiving?.name,
            education = education,
            unversity = university,
            graduationYear = graduationYear.toInt(),
            address = address,
            note = note,
            password = password,
            uid = preferencesHelper.uid
        )
        signUpVM.signUp(body)
    }

    private fun onSignUpResponse(states: UiStates?) {
        when (states) {
            UiStates.Loading -> {
                onLoading()
            }
            UiStates.Success -> {
                onSuccess()
            }
            UiStates.Empty -> {
                if (signUpVM.message != null)
                    activity?.snackBar(
                        signUpVM.message,
                        binding.viewRoot
                    )
                viewInputs()
            }
            UiStates.Error -> {
                if (signUpVM.message != null)
                    activity?.snackBar(
                        signUpVM.message,
                        binding.viewRoot
                    )
                viewInputs()
            }
            UiStates.NotFound -> {
                onNotFound()
            }
            UiStates.NoConnection -> {
                onNoConnection()
            }
        }
    }

    private fun onLoading() {
        binding.viewContainer.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.VISIBLE
    }

    private fun viewInputs() {
        binding.viewContainer.visibility = View.VISIBLE
        binding.loadingLayout.root.visibility = View.GONE
    }

    private fun onNoConnection() {
        binding.viewContainer.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.GONE
        binding.loadingLayout.noConnection.visibility = View.VISIBLE
    }

    private fun onNotFound() {
        binding.viewContainer.visibility = View.GONE
        binding.loadingLayout.root.visibility = View.VISIBLE
        binding.loadingLayout.loading.visibility = View.GONE
        binding.loadingLayout.error.visibility = View.VISIBLE
    }

    private fun onSuccess() {
        activity?.snackBar(requireActivity().getString(R.string.success), binding.viewRoot)
        goToHome()
        viewInputs()
        requireActivity().hideKeyboard()
    }

    private fun goToHome() {
        findNavController().navigate(R.id.action_signUp_fragment_to_home_fragment)
    }

}