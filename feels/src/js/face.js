const R = require('ramda')
const {fabric} = require('fabric')

const params_circle = R.curryN(4, function (stroke_width, x, y, r) {
  return {
    left: x - r,
    top: y - r,
    radius: r - stroke_width/2,
    strokeWidth: stroke_width,
  }
})

const params_disc = params_circle(0)

const coords_line_angular = R.curryN(4, function (len, deg, x, y) {
  const rad = (Math.PI/180) * deg
  return [
    x - (len/2) * Math.cos(rad),
    y + (len/2) * Math.sin(rad),
    x + (len/2) * Math.cos(rad),
    y - (len/2) * Math.sin(rad),
  ]
})

const outline = R.nAry(2, function (dims, props) {
  const {width, height} = dims
  if (width !== height) {
    throw new Error("unequal canvas dimensions")
  }
  const min_dim = Math.min(width, height)
  const stroke_width = min_dim * 0.025
  return new fabric.Circle(R.merge({
    stroke: 'black',
    fill: '',
  }, params_circle(stroke_width,
                   width/2, height/2,
                   min_dim/2)))
})

const mouth = R.nAry(2, function (dims, props) {
  const {width, height} = dims
  const {happy} = props
  const length_flat = .6 * width
  const curve_percentage = happy / 10
  const max_curve = .25 * height
  const y_pos = (2/3) * height
  const x_left_corner = (width - length_flat)/2
  const x_right_corner = width - x_left_corner
  const x_curve_handle_dist = length_flat / 4
  const y_curve_handle_pos = y_pos + max_curve * curve_percentage
  const str_bezier_curve = [
    'M ' + [x_left_corner, y_pos].join(' '),
    'C ' + [
      [x_left_corner + x_curve_handle_dist,
       y_curve_handle_pos].join(', '),
      [x_right_corner - x_curve_handle_dist,
       y_curve_handle_pos].join(', '),
      [x_right_corner, y_pos].join(', '),
    ].join(', '),
  ].join(' ')
  return new fabric.Path(str_bezier_curve, {
    fill: '',
    stroke: 'black',
    strokeWidth: Math.max(width, height)/90,
  })
})

const EYE_WIDTH = .4
const EYE_HEIGHT = 1/3
const EYE_R_MAX = 1/15

const eyes = R.nAry(2, function (dims, props) {
  const {width, height} = dims
  const {sleepy} = props
  const min_dim = Math.min(width, height)
  const eye_r_max = EYE_R_MAX * min_dim
  const eye_r_min = eye_r_max / 3
  const eye_r = eye_r_max - (eye_r_max - eye_r_min) * (sleepy/10)
  const y_pos = EYE_HEIGHT * height
  const x_left = (width - width * EYE_WIDTH) / 2
  const x_right = width - x_left
  return R.map(R.compose(
    R.constructN(1, fabric.Circle),
    R.merge({
      fill: 'black',
    }),
    params_disc(R.__, y_pos, eye_r)
  ), [x_left, x_right])
})

const eyebrows = R.nAry(2, function (dims, props) {
  const {width, height} = dims
  const {grumpy} = props
  const min_dim = Math.min(width, height)
  const stroke_width = min_dim * 0.015
  const y_pos = (EYE_HEIGHT * height -
                 stroke_width -
                 min_dim * EYE_R_MAX * 1.1)
  const x_left = (width - width * EYE_WIDTH) / 2
  const x_right = width - x_left
  const len = min_dim * EYE_R_MAX * 2
  const deg = 30 * (grumpy / 10) - 5
  return R.map(R.compose(
    R.constructN(2, fabric.Line)(R.__, {
      fill: 'black',
      stroke: 'black',
      strokeWidth: stroke_width,
    }),
    R.apply(coords_line_angular(len, R.__, R.__, y_pos))
  ), [
    [-1 * deg, x_left],
    [deg, x_right]
  ])
})

const face = function (emoji_props, canvas_el) {
  const canvas = new fabric.StaticCanvas(canvas_el)
  R.compose(
    R.forEach((drawable) => canvas.add(drawable)),
    R.filter(R.identity),
    R.flatten,
    R.map((fn) => fn(R.pick(['width', 'height'], canvas_el),
                     emoji_props))
  )([
    outline,
    mouth,
    eyes,
    eyebrows,
  ])
}

var exports = face

module.exports = exports










