Demonstrates what seems to be a bug with custom types and compose navigation.

In the main activity, there's a class called `Props` that gets updated (and forces a recomposition) every second.

The UI shows a screen with 3 NavHosts.

The first NavHost has a start destination with a custom type.
It lives in a separate composable, and re-navigates to its start destination every time the props get updated. 

The second NavHost is identical to the first, except it is defined in the same composable as the `props` class is being mutated.

The third NavHost is also identical to the first, other than the fact that its start destination does not have a custom type.

https://github.com/user-attachments/assets/750b281e-f7ed-42fa-977b-9aa8f6d22c74

