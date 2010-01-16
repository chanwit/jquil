@document class User {
    String name
    Integer age

    static mapping = {
        id(cache: true)
        name(index: true)
    }
}

/**
    User.find(id)
    User.findAll(id: id)
    User.all(id: [1, 2, 3])
**/