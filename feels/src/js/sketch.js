const {fabric} = require('fabric')

const sketch = function (canvas_el) {

  const canvas = new fabric.StaticCanvas(canvas_el)

  const rect = new fabric.Rect({
    left: 20,
    top: 20,
    fill: 'green',
    width: 20,
    height: 20,
  })

  const circ = new fabric.Circle({
    left: 50,
    top: 20,
    fill: 'blue',
    radius: 10,
  })

  const line1 = new fabric.Line([
    80, 30, 120, 30
  ], {
    fill: 'red',
    stroke: 'red',
    strokeWidth: 3,
  })

  const line2 = new fabric.Line([
    130, 20, 170, 40
  ], {
    fill: 'red',
    stroke: 'red',
    strokeWidth: 3,
  })


  const line3 = new fabric.Line([
    180, 40, 220, 20
  ], {
    fill: 'red',
    stroke: 'red',
    strokeWidth: 3,
  })

  const curve1 = new fabric.Path(
    ['M 20 60',
     'C 30, 40, 50, 40, 60, 60',
    ].join(' ')
    , {
      fill: '',
      stroke: 'black',
      strokeWidth: 3,
      })


  const curve2 = new fabric.Path(
    ['M 70 60',
     'C 80, 50, 100, 50, 110, 60',
    ].join(' ')
    , {
      fill: '',
      stroke: 'black',
      strokeWidth: 3,
      })


  const curve3 = new fabric.Path(
    ['M 120 60',
     'C 130, 70, 150, 70, 160, 60',
    ].join(' ')
    , {
      fill: '',
      stroke: 'black',
      strokeWidth: 3,
      })


  const curve4 = new fabric.Path(
    ['M 170 60',
     'C 180, 80, 200, 80, 210, 60',
    ].join(' ')
    , {
      fill: '',
      stroke: 'black',
      strokeWidth: 3,
      })

  canvas.add(rect)
  canvas.add(circ)
  canvas.add(line1)
  canvas.add(line2)
  canvas.add(line3)
  canvas.add(curve1)
  canvas.add(curve2)
  canvas.add(curve3)
  canvas.add(curve4)

}

var exports = sketch

module.exports = exports
