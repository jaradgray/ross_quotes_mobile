This branch was created off of @8742dc5 just before 1.0.0 release, where I was examining preventing the crash from denying Storage permission after previously granting it, by saving a copy of the Contact's image on EditContactFragment save button click.

Prior state:
 - Contact image ImageViews' sources are set with setImageUri()
 - app can crash if it tries to set a ImageView's source with a "content://*" Uri when Storage permission is denied

Current branch state:
 - save a scaled Bitmap to a File in app-specific external storage

---

Relevant files:
 - EditContactFragment.java
 - EditContactFragmentViewModel.java
 - Util.java

Notes:
 - Doesn't display contact image in notification
 - Doesn't delete saved image files on Contact deleted