This branch was created off of @8742dc5 just before 1.0.0 release, where I was examining preventing the crash from denying Storage permission after previously granting it, by saving a copy of the Contact's image on EditContactFragment save button click.

Prior state:
 - Contact image ImageViews' sources are set with setImageUri()
 - app can crash if it tries to set a ImageView's source with a "content://*" Uri when Storage permission is denied

Current branch state:
 - create an in-memory, scaled Bitmap from the Uri selected in EditContactFragment.onActivityResult()
 - save the in-memory Bitmap to a File in app-specific external storage on EditContactFragment save button click

---

Relevant files:
 - EditContactFragment.java
 - EditContactFragmentViewModel.java
 - Util.java

Notes:
 - Doesn't display contact image in notification
    - using BitmapFactory.decodeFile() fixed this, but then it wouldn't show images for bundled Contacts, whose images are in /drawable
 - Doesn't delete saved image files on Contact deleted