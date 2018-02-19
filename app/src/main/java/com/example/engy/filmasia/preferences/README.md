# Shared Preference
Here we practise how to use shared preferences to make customized settings for the user.
We used CheckBoxPreference, ListPreference, and EditTextPreference.
(1) Create your settings activity: 
        Make it as an option in the actionbar menu. Replace the layout in this activity xml with a fragment.
(2) Make a prefernece fragment:
        Override onCreatePreferences method and use addPreferencesFromResource passing it your fragment xml id.
(3) Make the preferences change in the UI without need to close the app and open it again ( recreate the view):
        By implementing onSharedPreferenceChanged in your mainActivity -> and overriding onSharedPreferenceChanged, putting our required changes in it,then registerthis listener in onCreate method and unregister it in onDestroy
(4) If needed in cases like ListPreference, EditTextPreference add preference summary to show the current applied settings.        
