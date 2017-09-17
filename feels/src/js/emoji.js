const face = require('./face')

const draw = function (emoji_props, canvas_el) {
  face(emoji_props, canvas_el)
}

var exports = {}

exports.draw = draw

module.exports = exports
