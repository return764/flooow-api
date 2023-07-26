project: flooow-api


## Tech
- [ ] add global extension function, like list.toVO or list.toPO?

## Process

- [X] support CRUD for action or node by websocket
- [X] support action status synchronization
- [X] optimize task execution process, run task one by one
- [X] support graph execution
- [X] support save&load action options
- [X] support option input type
- [X] support build task in runtime with input type
- [X] support task runtime message transform
- [ ] support task runtime error handler
- [X] support validate task
- [ ] support multipart graph

## Optimize
- [X] refactor option field autofill logic
- [X] refactor MessageHandler
- [ ] enhance the graph structure algorithm, make it support distinguish if there are two graph or have circle
- [X] extract AbstractAction

## Bug Fix

- [X] fix error of zIndex serialization
