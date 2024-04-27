# PT-Android-Cartify

Cartify is a simple app where users can choose between a list of product and check how much cost
them. Each product can have a discount, so users can benefit from it.

## Project structure

This app is written fully in Kotlin and Compose, and implements a Clean architecture, with:

- View: show data to the user
- ViewModel fetch data and pass it to the view
- Repository: load data from data sources and mapped it to user objects
- Data sources: load data from backend

## Technologies used

- Jetpack Compose
- Jetpack Compose Navigation
- ViewModel and LiveData
- Hilt
- Retrofit
- Moshi
- Coroutines
- MockK
- Compose test

## Other considerations

- I have not implemented a view model in CartScreen because there was no business logic needed in
  this view.
- I could have pass view model instance to Product list composable, and do the view model method
  calls from the view, but that would have done the view more difficult to test.
- I have created Discount as an enum because, using 'when' from Kotlin, whenever it is necessary to
  add a new discount, this 'when' will advice us where we need to add this new type, that way we can
  avoid future errors.
- I have included discount price logic inside this enum, that way is easier to locate where we need
  to make changes whenever we add/modify/remove a discount.
- I have included the correspondent discount while mapping Product object from data source, to have
  it located whenever it is needed to add/modify/remove a product discount.
- In order to avoid open Cart screen multiple times by clicking 'View cart' button several times, I
  have blocked button during 2 seconds. There is an easier solution (dropUnlessResume) to do this,
  but it is in Compose navigation version 2.8.0, and this is an alpha version, so I decided not to
  include it.
- I have not extracted strings or dimensions to a resource file, because I thought it was not
  necessary for this small app.
- For unit testing, I have preferred to use mocks instead of fakes because it was faster, but it
  depends on how personal do you want the dependency behaviour.

## Future improvements

As a future improvement, would be great to implement a Room database and save cart in it, so user
can close the app and keep selected products safe.
