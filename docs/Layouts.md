# Creating Layouts

## Scaffolds

A scaffold is typically used for each screen to provide padding that adapts to the Portal composable, providing padding values for the header and footer.

```kt
@Composable
fun ExampleListScreen() {
    val viewModel: ExampleListModel = viewModel { ExampleListModel() }
    val state by viewModel.state.collectAsState()

    Scaffold {
        // content goes here
    }
}
```

Layouts generally follow the typical pattern found in Compose: Rows, Columns, and LazyColumns. Overloads are used to specify theme-defined spacing between items. A Spacing value is provided from this enum class:

```kt
enum class Spacing { Unit, Grouped, Spaced }
```

* `Spacing.Unit` is used for ui elements that should appear as a single, cohesive unit
* `Spacing.Grouped` is used for a set of ui elements that should appear as a group
* `Spacing.Spaced` is used for ui elements that should appear unrelated

## Rows, Columnns, LazyColumns

Layouts that involve a list of items are typically implemented with a LazyColumn:

```kt
LazyColumn(Spacing.Unit) {
    items(state.items) { item ->
        Text(item.label)
    }
}
```

Cohesive UI elements are defined as Rows and Columns with Spacing.Unit:

```kt
Row(Spacing.Unit) {
    Image()
    Column(Spacing.Unit) {
        H2(item.headline)
        Text(item.description)
    }
}
```

## Clouds (UI Dialogs)

Dialogs/Popups are defined using the Cloud function that takes a visibility property from the UI state object and a method from the viewmodel to toggle/dismiss the popup:

```kt
Cloud(state.isCreatingItem, viewModel::toggleIsCreatingItem) {
        Controls {
            TextField(state.newLabel, viewModel::setLabel)
            Button("Add", onClick = viewModel::createNewItem)
        }
    }
```

Somewhere else in the Composable hierarchy might be a button that toggles the popup:

```kt
Button("Create", onClick = viewModel::toggleIsCreatingItem)
```