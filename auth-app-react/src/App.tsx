import './App.css'
import { Button } from "@/components/ui/button"
import { Calendar } from "@/components/ui/calendar"
import { useState } from 'react'

function App() {
const [date, setDate] = useState<Date | undefined>(new Date())

  return (
    <div className='p-10'>
      <h1 className='text-3xl font-bold'>Hello auth frontend!</h1>
      <Button variant={'destructive'}>click me</Button>
      <Calendar
        mode='single'
        selected={date}
        onSelect={setDate}
        className='rounded-lg border'
        captionLayout='dropdown'
      />
    </div>
  )
}

export default App
